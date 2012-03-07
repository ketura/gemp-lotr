package com.gempukku.lotro.cards.set18.gondor;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.ShuffleCardsFromHandIntoDeckEffect;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Possession • Shield
 * Game Text: Bearer must be a [GONDOR] Man. The minion archery total is -1. Regroup: Discard this possession from play
 * to make each opponent shuffle his or her hand into his or her draw deck and draw 8 cards.
 */
public class Card18_057 extends AbstractAttachableFPPossession {
    public Card18_057() {
        super(1, 0, 0, Culture.GONDOR, PossessionClass.SHIELD, "Shield of the White Tree");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GONDOR, Race.MAN);
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new ArcheryTotalModifier(self, Side.SHADOW, -1));
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            for (String opponentId : GameUtils.getOpponents(game, playerId)) {
                action.appendEffect(
                        new ShuffleCardsFromHandIntoDeckEffect(self, opponentId, new HashSet<PhysicalCard>(game.getGameState().getHand(opponentId))));
                action.appendEffect(
                        new DrawCardsEffect(action, opponentId, 8));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
