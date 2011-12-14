package com.gempukku.lotro.cards.set12.uruk_hai;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: At the start of the regroup phase, you may remove (1) to make an [URUK-HAI] minion gain muster until the
 * end of the regroup phase. (At the start of the regroup phase, you may discard a card from hand to draw a card.)
 */
public class Card12_160 extends AbstractPermanent {
    public Card12_160() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.URUK_HAI, Zone.SUPPORT, "Worthy of Mordor");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.REGROUP)
                && game.getGameState().getTwilightPool() >= 1) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(1));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose an URUK-HAI minion", Culture.URUK_HAI, CardType.MINION) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new KeywordModifier(self, card, Keyword.MUSTER), Phase.REGROUP));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
