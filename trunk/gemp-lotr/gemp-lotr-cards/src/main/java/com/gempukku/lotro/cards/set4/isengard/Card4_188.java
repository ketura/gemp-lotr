package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.ExhaustCharacterEffect;
import com.gempukku.lotro.cards.modifiers.CantDiscardFromPlayModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.KillResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 6
 * Type: Minion • Uruk-Hai
 * Strength: 12
 * Vitality: 3
 * Site: 5
 * Game Text: Tracker. Fierce. Unbound Hobbits may not be discarded. Response: If an unbound Hobbit is killed, exert
 * this minion twice to exhaust a companion.
 */
public class Card4_188 extends AbstractMinion {
    public Card4_188() {
        super(6, 12, 3, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk Hunter");
        addKeyword(Keyword.TRACKER);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new CantDiscardFromPlayModifier(self, "Unbound Hobbits may not be discarded", Filters.and(Filters.unboundCompanion(), Filters.race(Race.HOBBIT)), Filters.any()));
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.KILL
                && PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), null, self, 0)
                && PlayConditions.canExert(self, game, 2, Filters.sameCard(self))) {
            KillResult killResult = (KillResult) effectResult;
            if (Filters.filter(killResult.getKilledCards(), game.getGameState(), game.getModifiersQuerying(), Filters.unboundCompanion(), Filters.race(Race.HOBBIT)).size() > 0) {
                final ActivateCardAction action = new ActivateCardAction(self);
                action.appendCost(
                        new ExertCharactersEffect(self, self));
                action.appendCost(
                        new ExertCharactersEffect(self, self));
                action.appendEffect(
                        new ChooseActiveCardEffect(self, playerId, "Choose companion", Filters.type(CardType.COMPANION), Filters.canExert(self)) {
                            @Override
                            protected void cardSelected(LotroGame game, PhysicalCard card) {
                                action.insertEffect(
                                        new ExhaustCharacterEffect(playerId, action, card));
                            }
                        });
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
