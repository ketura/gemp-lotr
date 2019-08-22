package com.gempukku.lotro.cards.set32.esgaroth;

import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.PreventCardEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.modifiers.AllyParticipatesInArcheryFireAndSkirmishesModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Free
 * Culture: Esgaroth
 * Twilight Cost: 2
 * Type: Ally • Home 6 • Man
 * Strength: 6
 * Vitality: 3
 * Site: 6
 * Game Text: Archer. Each [Esgaroth] Man bearing a weapon may participate in archery fire and skirmishes.
 * Response: If another [Esgaroth] ally is about to take a wound, exert Percy to prevent that wound.
 */
public class Card32_019 extends AbstractAlly {
    public Card32_019() {
        super(2, SitesBlock.HOBBIT, 6, 6, 3, Race.MAN, Culture.ESGAROTH, "Percy", "Bard's Lieutenant", true);
        addKeyword(Keyword.ARCHER);
    }
    
    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new AllyParticipatesInArcheryFireAndSkirmishesModifier(self, Filters.and(Culture.ESGAROTH, CardType.ALLY, Filters.hasAttached(Filters.or(PossessionClass.HAND_WEAPON, PossessionClass.RANGED_WEAPON)))));
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Filters.and(Culture.ESGAROTH, CardType.ALLY, Filters.not(self)))
                && PlayConditions.canExert(self, game, self)) {
            final WoundCharactersEffect woundEffects = (WoundCharactersEffect) effect;

            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose an Esgaroth ally", Filters.in(woundEffects.getAffectedCardsMinusPrevented(game)), Filters.and(Culture.ESGAROTH, CardType.ALLY, Filters.not(self))) {
                        @Override
                        public void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new PreventCardEffect(woundEffects, card));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
