package com.gempukku.lotro.game.timing.rules;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.AbstractActionProxy;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.game.GameUtils;
import com.gempukku.lotro.game.PlayUtils;
import com.gempukku.lotro.game.actions.Action;

import java.util.LinkedList;
import java.util.List;

public class PlayCardInPhaseRule {
    private final DefaultActionsEnvironment actionsEnvironment;

    public PlayCardInPhaseRule(DefaultActionsEnvironment actionsEnvironment) {
        this.actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends Action> getPhaseActions(String playerId, DefaultGame game) {
                        final Phase phase = game.getGameState().getCurrentPhase();
                        Side side = GameUtils.getSide(game, playerId);
                        if (phase == Phase.FELLOWSHIP) {
                            if (GameUtils.isFP(game, playerId)) {
                                List<Action> result = new LinkedList<>();
                                for (PhysicalCard card : Filters.filter(game.getGameState().getHand(playerId), game, side,
                                        Filters.or(Filters.and(CardType.EVENT, Keyword.FELLOWSHIP), Filters.not(CardType.EVENT)))) {
                                    if (PlayUtils.checkPlayRequirements(game, card, Filters.any, 0, 0, false, false, true))
                                        result.add(PlayUtils.getPlayCardAction(game, card, 0, Filters.any, false));
                                }
                                return result;
                            }
                        } else if (phase == Phase.SHADOW) {
                            if (GameUtils.isShadow(game, playerId)) {
                                List<Action> result = new LinkedList<>();
                                for (PhysicalCard card : Filters.filter(game.getGameState().getHand(playerId), game, side,
                                        Filters.or(Filters.and(CardType.EVENT, Keyword.SHADOW), Filters.not(CardType.EVENT)))) {
                                    if (PlayUtils.checkPlayRequirements(game, card, Filters.any, 0, 0, false, false, true))
                                        result.add(PlayUtils.getPlayCardAction(game, card, 0, Filters.any, false));
                                }
                                return result;
                            }
                        } else {
                            final Keyword phaseKeyword = PlayUtils.PhaseKeywordMap.get(game.getGameState().getCurrentPhase());
                            if (phaseKeyword != null) {
                                List<Action> result = new LinkedList<>();
                                for (PhysicalCard card : Filters.filter(game.getGameState().getHand(playerId), game, side,
                                        Filters.and(CardType.EVENT, phaseKeyword))) {
                                    if (PlayUtils.checkPlayRequirements(game, card, Filters.any, 0, 0, false, false, true))
                                        result.add(PlayUtils.getPlayCardAction(game, card, 0, Filters.any, false));
                                }
                                return result;
                            }
                        }

                        return null;
                    }
                }
        );
    }
}
