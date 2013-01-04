package com.gempukku.lotro.cards.set17.men;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.StackCardFromDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 2
 * Type: Possession • Support Area
 * Game Text: Each time a [MEN] minion is discarded during the maneuver phase, you may stack it here.
 * Skirmish: Discard this possession to make a [MEN] Man strength +2.
 */
public class Card17_066 extends AbstractPermanent {
    public Card17_066() {
        super(Side.SHADOW, 2, CardType.POSSESSION, Culture.MEN, Zone.SUPPORT, "Wildman's Oath");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachDiscardedFromPlay(game, effectResult, Culture.MEN, CardType.MINION)
                && PlayConditions.isPhase(game, Phase.MANEUVER)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            PhysicalCard discardedCard = ((DiscardCardsFromPlayResult) effectResult).getDiscardedCard();
            action.setTriggerIdentifier(self.getCardId() + "-" + discardedCard.getCardId());
            action.setText("Stack " + GameUtils.getCardLink(discardedCard));
            action.appendEffect(
                    new StackCardFromDiscardEffect(discardedCard, self));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Culture.MEN, Race.MAN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
