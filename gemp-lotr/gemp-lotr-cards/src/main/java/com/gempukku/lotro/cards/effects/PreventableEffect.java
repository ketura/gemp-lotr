package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.cards.actions.PreventSubAction;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.timing.AbstractSubActionEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.*;

public class PreventableEffect extends AbstractSubActionEffect {
    private CostToEffectAction _action;
    private Effect _effectToExecute;
    private Iterator<String> _choicePlayers;
    private PreventionCost _preventionCost;

    public PreventableEffect(CostToEffectAction action, Effect effectToExecute, String[] choicePlayers, PreventionCost preventionCost) {
        this(action, effectToExecute, Arrays.asList(choicePlayers), preventionCost);
    }

    public PreventableEffect(CostToEffectAction action, Effect effectToExecute, String choicePlayer, PreventionCost preventionCost) {
        this(action, effectToExecute, Collections.singletonList(choicePlayer), preventionCost);
    }

    public PreventableEffect(CostToEffectAction action, Effect effectToExecute, List<String> choicePlayers, PreventionCost preventionCost) {
        _action = action;
        _effectToExecute = effectToExecute;
        _choicePlayers = choicePlayers.iterator();
        _preventionCost = preventionCost;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _effectToExecute.isPlayableInFull(game);
    }

    @Override
    public Collection<? extends EffectResult> playEffect(LotroGame game) {
        final PreventSubAction subAction = new PreventSubAction(_action, _effectToExecute, _choicePlayers, _preventionCost);
        processSubAction(game, subAction);
        return null;
    }

    public static interface PreventionCost {
        public Effect createPreventionCostForPlayer(SubAction subAction, String playerId);
    }
}
