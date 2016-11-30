package com.gempukku.lotro.cards.set30.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.common.*;
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
 * Set: Main Deck
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Companion • Dwarf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Damage +1 Response: If an [DWARVEN] companion is about to take a wound, exert Fili and add (1)
 * to prevent that wound.
 */
public class Card30_010 extends AbstractCompanion {
    public Card30_010() {
        super(2, 6, 3, 6, Culture.DWARVEN, Race.DWARF, null, "Fili", "Brother of Kili", true);
        addKeyword(Keyword.DAMAGE, 1); 
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Culture.DWARVEN, CardType.COMPANION)
                && PlayConditions.canExert(self, game, self)) {
            final WoundCharactersEffect woundEffects = (WoundCharactersEffect) effect;

            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendCost(
                    new AddTwilightEffect(self, 1));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a companion", Filters.in(woundEffects.getAffectedCardsMinusPrevented(game)), Culture.DWARVEN, CardType.COMPANION) {
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