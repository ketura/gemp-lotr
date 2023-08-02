package com.gempukku.lotro.effects;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.actions.lotronly.PreventSubAction;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class PreventableEffect extends AbstractSubActionEffect {
    private final CostToEffectAction _action;
    private final Effect _effectToExecute;
    private final Effect _insteadEffect;
    private final Iterator<String> _choicePlayers;
    private final PreventionCost _preventionCost;

    public PreventableEffect(CostToEffectAction action, Effect effectToExecute, String[] choicePlayers, PreventionCost preventionCost) {
        this(action, effectToExecute, Arrays.asList(choicePlayers), preventionCost, null);
    }

    public PreventableEffect(CostToEffectAction action, Effect effectToExecute, String choicePlayer, PreventionCost preventionCost) {
        this(action, effectToExecute, Collections.singletonList(choicePlayer), preventionCost, null);
    }

    public PreventableEffect(CostToEffectAction action, Effect effectToExecute, String choicePlayer, PreventionCost preventionCost, Effect insteadEffect) {
        this(action, effectToExecute, Collections.singletonList(choicePlayer), preventionCost, insteadEffect);
    }

    public PreventableEffect(CostToEffectAction action, Effect effectToExecute, List<String> choicePlayers, PreventionCost preventionCost) {
        this(action, effectToExecute, choicePlayers, preventionCost, null);
    }

    public PreventableEffect(CostToEffectAction action, Effect effectToExecute, List<String> choicePlayers, PreventionCost preventionCost, Effect insteadEffect) {
        _action = action;
        _effectToExecute = effectToExecute;
        _insteadEffect = insteadEffect;
        _choicePlayers = choicePlayers.iterator();
        _preventionCost = preventionCost;
    }

    @Override
    public String getText(DefaultGame game) {
        return null;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return _effectToExecute.isPlayableInFull(game);
    }

    @Override
    public void playEffect(DefaultGame game) {
        final PreventSubAction subAction = new PreventSubAction(_action, _effectToExecute, _choicePlayers, _preventionCost, _insteadEffect);
        processSubAction(game, subAction);
    }

    public static interface PreventionCost {
        public Effect createPreventionCostForPlayer(CostToEffectAction subAction, String playerId);
    }
}
