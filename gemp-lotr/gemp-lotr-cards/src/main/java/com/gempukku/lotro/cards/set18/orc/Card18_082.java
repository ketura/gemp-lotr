package com.gempukku.lotro.cards.set18.orc;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RemoveCardsFromTheGameEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.ShuffleDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 3
 * Type: Possession • Support Area
 * Game Text: To play, spot an [ORC] minion. Each time the fellowship moves in the regroup phase, you may discard
 * this possession to search the Free Peoples player's draw deck and choose 2 Free Peoples cards found there.
 * Remove those cards from the game.
 */
public class Card18_082 extends AbstractPermanent {
    public Card18_082() {
        super(Side.SHADOW, 3, CardType.POSSESSION, Culture.ORC, Zone.SUPPORT, "Grond", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.ORC, CardType.MINION);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.moves(game, effectResult)
                && PlayConditions.isPhase(game, Phase.REGROUP)
                && PlayConditions.canSelfDiscard(self, game)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseArbitraryCardsEffect(playerId, "Choose cards to remove", game.getGameState().getDeck(game.getGameState().getCurrentPlayerId()), Side.FREE_PEOPLE, 2, 2) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            action.insertEffect(
                                    new RemoveCardsFromTheGameEffect(playerId, self, selectedCards));
                        }
                    });
            action.appendEffect(
                    new ShuffleDeckEffect(game.getGameState().getCurrentPlayerId()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
