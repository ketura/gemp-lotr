package com.gempukku.lotro.cards.set17.men;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.StackCardFromDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardStackedCardsEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 2
 * Type: Possession • Support Area
 * Game Text: Each time a [MEN] minion is discarded during the maneuver phase, you may stack it here.
 * Response: Discard a minion stacked here or discard this to prevent a wound to a [MEN] minion.
 */
public class Card17_041 extends AbstractPermanent {
    public Card17_041() {
        super(Side.SHADOW, 2, CardType.POSSESSION, Culture.MEN, "Ceremonial Armor");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachDiscardedFromPlay(game, effectResult, Culture.MEN, CardType.MINION)
                && PlayConditions.isPhase(game, Phase.MANEUVER)) {
            DiscardCardsFromPlayResult discardResult = (DiscardCardsFromPlayResult) effectResult;
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.setTriggerIdentifier(self.getCardId()+"-"+discardResult.getDiscardedCard());
            action.setText("Stack " + GameUtils.getFullName(discardResult.getDiscardedCard()));
            action.appendEffect(
                    new StackCardFromDiscardEffect(discardResult.getDiscardedCard(), self));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Culture.MEN, CardType.MINION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new ChooseAndDiscardStackedCardsEffect(action, playerId, 1, 1, self, CardType.MINION) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Discard a minion stacked here";
                        }
                    });
            possibleCosts.add(
                    new SelfDiscardEffect(self));
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, (WoundCharactersEffect) effect, playerId, "Choose a minion", Culture.MEN, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
