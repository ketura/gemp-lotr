package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.AssignmentResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssignmentEffect implements Effect {
    private Map<PhysicalCard, List<PhysicalCard>> _assignments;
    private String _text;
    private String _playerId;

    public AssignmentEffect(String playerId, Map<PhysicalCard, List<PhysicalCard>> assignments, String text) {
        _playerId = playerId;
        // Sanitize the assignments
        _assignments = new HashMap<PhysicalCard, List<PhysicalCard>>();
        for (Map.Entry<PhysicalCard, List<PhysicalCard>> physicalCardListEntry : assignments.entrySet()) {
            PhysicalCard fpChar = physicalCardListEntry.getKey();
            List<PhysicalCard> minions = physicalCardListEntry.getValue();
            if (minions != null && minions.size() > 0)
                _assignments.put(fpChar, minions);
        }
        _text = text;
    }

    public AssignmentEffect(String playerId, PhysicalCard fpChar, List<PhysicalCard> minions, String text) {
        this(playerId, Collections.singletonMap(fpChar, minions), text);
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.ASSIGNMENT;
    }

    @Override
    public String getText(LotroGame game) {
        return _text;
    }

    @Override
    public EffectResult[] playEffect(LotroGame game) {
        game.getGameState().sendMessage(_playerId + " assigns minion(s) to skirmish");
        for (Map.Entry<PhysicalCard, List<PhysicalCard>> physicalCardListEntry : _assignments.entrySet()) {
            PhysicalCard fpChar = physicalCardListEntry.getKey();
            List<PhysicalCard> minions = physicalCardListEntry.getValue();
            game.getGameState().assignToSkirmishes(fpChar, minions);
        }
        return new EffectResult[]{new AssignmentResult(_assignments)};
    }
}
