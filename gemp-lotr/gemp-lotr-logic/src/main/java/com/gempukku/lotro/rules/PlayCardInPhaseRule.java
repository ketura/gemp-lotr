package com.gempukku.lotro.rules;

import com.gempukku.lotro.actions.AbstractActionProxy;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.actions.Action;
import com.gempukku.lotro.rules.lotronly.LotroGameUtils;
import com.gempukku.lotro.rules.lotronly.LotroPlayUtils;

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
                        Side side = LotroGameUtils.getSide(game, playerId);
                        if (phase == Phase.FELLOWSHIP) {
                            if (LotroGameUtils.isFP(game, playerId)) {
                                List<Action> result = new LinkedList<>();
                                for (LotroPhysicalCard card : Filters.filter(game.getGameState().getHand(playerId), game, side,
                                        Filters.or(Filters.and(CardType.EVENT, Keyword.FELLOWSHIP), Filters.not(CardType.EVENT)))) {
                                    if (LotroPlayUtils.checkPlayRequirements(game, card, Filters.any, 0, 0, false, false, true))
                                        result.add(PlayUtils.getPlayCardAction(game, card, 0, Filters.any, false));
                                }
                                return result;
                            }
                        } else if (phase == Phase.SHADOW) {
                            if (LotroGameUtils.isShadow(game, playerId)) {
                                List<Action> result = new LinkedList<>();
                                for (LotroPhysicalCard card : Filters.filter(game.getGameState().getHand(playerId), game, side,
                                        Filters.or(Filters.and(CardType.EVENT, Keyword.SHADOW), Filters.not(CardType.EVENT)))) {
                                    if (LotroPlayUtils.checkPlayRequirements(game, card, Filters.any, 0, 0, false, false, true))
                                        result.add(PlayUtils.getPlayCardAction(game, card, 0, Filters.any, false));
                                }
                                return result;
                            }
                        } else {
                            final Keyword phaseKeyword = PlayUtils.PhaseKeywordMap.get(game.getGameState().getCurrentPhase());
                            if (phaseKeyword != null) {
                                List<Action> result = new LinkedList<>();
                                for (LotroPhysicalCard card : Filters.filter(game.getGameState().getHand(playerId), game, side,
                                        Filters.and(CardType.EVENT, phaseKeyword))) {
                                    if (LotroPlayUtils.checkPlayRequirements(game, card, Filters.any, 0, 0, false, false, true))
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
