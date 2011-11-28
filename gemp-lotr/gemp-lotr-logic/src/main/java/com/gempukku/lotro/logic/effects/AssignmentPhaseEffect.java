package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.results.AssignmentResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AssignmentPhaseEffect extends AbstractEffect {
    private Map<PhysicalCard, Set<PhysicalCard>> _assignments;
    private String _text;
    private String _playerId;

    public AssignmentPhaseEffect(String playerId, Map<PhysicalCard, Set<PhysicalCard>> assignments, String text) {
        _playerId = playerId;
        // Sanitize the assignments
        _assignments = new HashMap<PhysicalCard, Set<PhysicalCard>>();
        for (Map.Entry<PhysicalCard, Set<PhysicalCard>> physicalCardListEntry : assignments.entrySet()) {
            PhysicalCard fpChar = physicalCardListEntry.getKey();
            Set<PhysicalCard> minions = physicalCardListEntry.getValue();
            if (minions != null && minions.size() > 0)
                _assignments.put(fpChar, minions);
        }
        _text = text;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public String getText(LotroGame game) {
        return _text;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (_assignments.size() > 0)
            game.getGameState().sendMessage(_playerId + " assigns characters to skirmish");
        for (Map.Entry<PhysicalCard, Set<PhysicalCard>> physicalCardListEntry : _assignments.entrySet()) {
            PhysicalCard fpChar = physicalCardListEntry.getKey();
            Set<PhysicalCard> minions = physicalCardListEntry.getValue();
            game.getGameState().assignToSkirmishes(fpChar, minions);

            game.getActionsEnvironment().emitEffectResult(new AssignmentResult(_playerId, fpChar, minions));
            for (PhysicalCard minion : minions)
                game.getActionsEnvironment().emitEffectResult(new AssignmentResult(_playerId, minion, fpChar));
        }
        return new FullEffectResult(true, true);
    }
}
