package com.gempukku.lotro.game.rules;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.AbstractActionProxy;
import com.gempukku.lotro.game.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.game.actions.lotronly.PlayEventAction;
import com.gempukku.lotro.game.actions.Action;
import com.gempukku.lotro.game.effects.Effect;
import com.gempukku.lotro.game.effects.EffectResult;
import com.gempukku.lotro.game.rules.lotronly.LotroGameUtils;
import com.gempukku.lotro.game.rules.lotronly.LotroPlayUtils;

import java.util.LinkedList;
import java.util.List;

public class PlayResponseEventRule {
    private final DefaultActionsEnvironment actionsEnvironment;

    public PlayResponseEventRule(DefaultActionsEnvironment actionsEnvironment) {
        this.actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends Action> getOptionalAfterActions(String playerId, DefaultGame game, EffectResult effectResult) {
                        List<Action> result = new LinkedList<>();
                        final Side side = LotroGameUtils.getSide(game, playerId);
                        for (LotroPhysicalCard responseEvent : Filters.filter(game.getGameState().getHand(playerId), game, side, CardType.EVENT, Keyword.RESPONSE)) {
                            if (LotroPlayUtils.checkPlayRequirements(game, responseEvent, Filters.any, 0, 0, false, false, false)) {
                                final List<PlayEventAction> actions = responseEvent.getBlueprint().getPlayResponseEventAfterActions(playerId, game, effectResult, responseEvent);
                                if (actions != null)
                                    result.addAll(actions);
                            }
                        }
                        return result;
                    }

                    @Override
                    public List<? extends Action> getOptionalBeforeActions(String playerId, DefaultGame game, Effect effect) {
                        List<Action> result = new LinkedList<>();
                        final Side side = LotroGameUtils.getSide(game, playerId);
                        for (LotroPhysicalCard responseEvent : Filters.filter(game.getGameState().getHand(playerId), game, side, CardType.EVENT, Keyword.RESPONSE)) {
                            if (LotroPlayUtils.checkPlayRequirements(game, responseEvent, Filters.any, 0, 0, false, false, false)) {
                                final List<PlayEventAction> actions = responseEvent.getBlueprint().getPlayResponseEventBeforeActions(playerId, game, effect, responseEvent);
                                if (actions != null)
                                    result.addAll(actions);
                            }
                        }
                        return result;
                    }
                }
        );
    }
}
