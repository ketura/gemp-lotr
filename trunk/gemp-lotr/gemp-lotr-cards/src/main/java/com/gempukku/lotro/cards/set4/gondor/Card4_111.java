package com.gempukku.lotro.cards.set4.gondor;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.cards.modifiers.VitalityModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Condition
 * Vitality: +1
 * Game Text: Bearer must be Faramir. Skirmish: Discard a [GONDOR] card from hand to make a Ringbound Man strength +1.
 */
public class Card4_111 extends AbstractAttachable {
    public Card4_111() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 0, Culture.GONDOR, null, "Boromir, My Brother", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Faramir");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new VitalityModifier(self, Filters.hasAttached(self), 1));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.GONDOR)).size() > 0) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, 1, Filters.culture(Culture.GONDOR)));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose Rin-bound Man", Filters.race(Race.MAN), Filters.keyword(Keyword.RING_BOUND)) {
                        @Override
                        protected void cardSelected(PhysicalCard card) {
                            action.insertEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.sameCard(card), 1), Phase.SKIRMISH));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
