package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.AssignmentResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssignmentEffect extends AbstractEffect {
    private Map<PhysicalCard, List<PhysicalCard>> _assignments;
    private String _text;

    public AssignmentEffect(Map<PhysicalCard, List<PhysicalCard>> assignments, String text) {
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

    public AssignmentEffect(PhysicalCard fpChar, List<PhysicalCard> minions, String text) {
        this(Collections.singletonMap(fpChar, minions), text);
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return true;
    }

    @Override
    public EffectResult getRespondableResult() {
        return new AssignmentResult(_assignments);
    }

    @Override
    public String getText() {
        return _text;
    }

    @Override
    public void playEffect(LotroGame game) {
        for (Map.Entry<PhysicalCard, List<PhysicalCard>> physicalCardListEntry : _assignments.entrySet()) {
            PhysicalCard fpChar = physicalCardListEntry.getKey();
            List<PhysicalCard> minions = physicalCardListEntry.getValue();
            game.getGameState().assignToSkirmishes(fpChar, minions);
        }
    }
}
