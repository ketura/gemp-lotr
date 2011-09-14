package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.actions.KillAction;
import com.gempukku.lotro.logic.actions.WoundAction;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.NormalSkirmishResult;
import com.gempukku.lotro.logic.timing.results.OverwhelmSkirmishResult;

import java.util.LinkedList;
import java.util.List;

public class ResolveSkirmishRule {
    private LotroGame _lotroGame;
    private DefaultActionsEnvironment _actionsEnvironment;

    public ResolveSkirmishRule(LotroGame lotroGame, DefaultActionsEnvironment actionsEnvironment) {
        _lotroGame = lotroGame;
        _actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        _actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends Action> getRequiredAfterTriggers(LotroGame lotroGame, EffectResult effectResult) {
                        if (effectResult.getType() == EffectResult.Type.RESOLVE_SKIRMISH) {
                            NormalSkirmishResult skirmishResult = (NormalSkirmishResult) effectResult;
                            List<PhysicalCard> winners = skirmishResult.getWinners();
                            int dmg = 1;
                            ModifiersQuerying modifiersQuerying = _lotroGame.getModifiersQuerying();
                            GameState gameState = _lotroGame.getGameState();
                            for (PhysicalCard winner : winners)
                                dmg += modifiersQuerying.getKeywordCount(gameState, winner, Keyword.DAMAGE);

                            List<PhysicalCard> losers = skirmishResult.getLosers();

                            List<Action> actions = new LinkedList<Action>();
                            for (PhysicalCard loser : losers) {
                                actions.add(new WoundAction(loser.getOwner(), loser, dmg));
                            }
                            return actions;
                        } else if (effectResult.getType() == EffectResult.Type.OVERWHELM_IN_SKIRMISH) {
                            OverwhelmSkirmishResult skirmishResult = (OverwhelmSkirmishResult) effectResult;
                            List<PhysicalCard> losers = skirmishResult.getLosers();

                            List<Action> actions = new LinkedList<Action>();
                            for (PhysicalCard loser : losers)
                                actions.add(new KillAction(loser));
                            return actions;
                        } else {
                            return null;
                        }
                    }
                }
        );
    }
}
