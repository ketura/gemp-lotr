package com.gempukku.lotro.cards.set4.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Signet: Theoden
 * Game Text: Valiant. Response: If an unbound companion is about to take a wound, exert Eowyn and add (1) to prevent
 * that wound.
 */
public class Card4_270 extends AbstractCompanion {
    public Card4_270() {
        super(2, 6, 3, 6, Culture.ROHAN, Race.MAN, Signet.THÉODEN, "Eowyn", true);
        addKeyword(Keyword.VALIANT);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Filters.unboundCompanion)
                && PlayConditions.canExert(self, game, Filters.sameCard(self))) {
            final WoundCharactersEffect woundEffects = (WoundCharactersEffect) effect;

            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendCost(
                    new AddTwilightEffect(self, 1));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a companion", Filters.in(woundEffects.getAffectedCardsMinusPrevented(game)), Filters.unboundCompanion) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new PreventCardEffect(woundEffects, card));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
