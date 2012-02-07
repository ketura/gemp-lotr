package com.gempukku.lotro.cards.set15.wraith;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Each time a Nazgul wins a skirmish, you may exert 2 Nazgul (or a Nazgul twice) to add a threat.
 * Assignment: Spot a Nazgul and remove (4) to play a Nazgul from your discard pile.
 */
public class Card15_182 extends AbstractPermanent {
    public Card15_182() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.WRAITH, Zone.SUPPORT, "A Shadow Fell Over Them");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Race.NAZGUL)
                && (PlayConditions.canExert(self, game, 1, 2, Race.NAZGUL)
                || PlayConditions.canExert(self, game, 2, 1, Race.NAZGUL))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new ChooseAndExertCharactersEffect(action, playerId, 2, 2, 1, Race.NAZGUL) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert 2 Nazgul";
                        }
                    });
            possibleCosts.add(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Race.NAZGUL) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert a Nazgul twice";
                        }
                    });
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new AddThreatsEffect(playerId, self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 4)
                && PlayConditions.canSpot(game, Race.NAZGUL)
                && PlayConditions.canPlayFromDiscard(playerId, game, 4, 0, Race.NAZGUL)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(4));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Race.NAZGUL));
            return Collections.singletonList(action);
        }
        return null;
    }
}
