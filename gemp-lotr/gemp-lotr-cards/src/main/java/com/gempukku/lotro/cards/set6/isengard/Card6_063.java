package com.gempukku.lotro.cards.set6.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.StackCardFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardStackedCardsEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. Each time a regroup action discards an [ISENGARD] Orc, you may stack that Orc
 * on this card. Shadow: Discard 2 cards stacked here and remove (1) to play an [ISENGARD] Orc from your discard pile.
 */
public class Card6_063 extends AbstractPermanent {
    public Card6_063() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.ISENGARD, Zone.SUPPORT, "Gnawing, Biting, Hacking, Burning");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (PlayConditions.isGettingDiscarded(effect, game, Culture.ISENGARD, Race.ORC)) {
            DiscardCardsFromPlayEffect discardEffect = (DiscardCardsFromPlayEffect) effect;
            if (game.getGameState().getCurrentPhase() == Phase.REGROUP
                    && discardEffect.getSource() != null) {
                List<OptionalTriggerAction> actions = new LinkedList<OptionalTriggerAction>();
                final Collection<PhysicalCard> discardedOrcs = Filters.filter(discardEffect.getAffectedCardsMinusPrevented(game), game.getGameState(), game.getModifiersQuerying(), Culture.ISENGARD, Race.ORC);
                for (final PhysicalCard discardedOrc : discardedOrcs) {
                    OptionalTriggerAction action = new OptionalTriggerAction(self);
                    action.setText("Stack " + GameUtils.getCardLink(discardedOrc));
                    action.appendEffect(
                            new StackCardFromPlayEffect(discardedOrc, self));
                    actions.add(action);
                }

                return actions;
            }
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SHADOW, self, 1)
                && game.getGameState().getStackedCards(self).size() >= 2
                && PlayConditions.canPlayFromDiscard(playerId, game, 1, Culture.ISENGARD, Race.ORC)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardStackedCardsEffect(action, playerId, 2, 2, self, Filters.any));
            action.appendCost(
                    new RemoveTwilightEffect(1));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game.getGameState().getDiscard(playerId), Culture.ISENGARD, Race.ORC));
            return Collections.singletonList(action);
        }
        return null;
    }
}
