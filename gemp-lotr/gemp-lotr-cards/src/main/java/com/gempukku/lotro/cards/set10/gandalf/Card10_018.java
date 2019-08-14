package com.gempukku.lotro.cards.set10.gandalf;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 5
 * Type: Companion • Ent
 * Strength: 12
 * Vitality: 4
 * Resistance: 6
 * Game Text: To play, spot 3 [GANDALF] companions. Regroup: Exert Treebeard twice to play a [GANDALF] condition from
 * your discard pile and make an opponent discard one of his or her conditions.
 */
public class Card10_018 extends AbstractCompanion {
    public Card10_018() {
        super(5, 12, 4, 6, Culture.GANDALF, Race.ENT, null, "Treebeard", "Keeper of the Watchwood", true);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 3, Culture.GANDALF, CardType.COMPANION);
    }

    @Override
    public List<ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canSelfExert(self, 2, game)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.GANDALF, CardType.CONDITION)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.GANDALF, CardType.CONDITION));
            action.appendEffect(
                    new ChooseOpponentEffect(playerId) {
                        @Override
                        protected void opponentChosen(String opponentId) {
                            action.appendEffect(
                                    new ChooseAndDiscardCardsFromPlayEffect(action, opponentId, 1, 1, Filters.owner(opponentId), CardType.CONDITION));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
