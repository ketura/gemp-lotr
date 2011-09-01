package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.actions.KillAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.LinkedList;
import java.util.List;

public class CharacterDeathRule {
    private DefaultActionsEnvironment _actionsEnvironment;

    public CharacterDeathRule(DefaultActionsEnvironment actionsEnvironment) {
        _actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        _actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
                        List<Action> actions = new LinkedList<Action>();

                        GameState gameState = game.getGameState();

                        List<PhysicalCard> characters = Filters.filterActive(gameState, game.getModifiersQuerying(),
                                Filters.or(Filters.type(CardType.ALLY), Filters.type(CardType.COMPANION), Filters.type(CardType.MINION)));
                        for (PhysicalCard character : characters)
                            if (gameState.getWounds(character) >= game.getModifiersQuerying().getVitality(gameState, character))
                                actions.add(new KillAction(character));

                        return actions;
                    }
                }
        );
    }
}
