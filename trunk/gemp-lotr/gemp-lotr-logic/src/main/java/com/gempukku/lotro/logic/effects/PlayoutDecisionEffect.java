package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.communication.UserFeedback;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.AwaitingDecision;
import com.gempukku.lotro.logic.timing.Cost;
import com.gempukku.lotro.logic.timing.CostResolution;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class PlayoutDecisionEffect extends UnrespondableEffect implements Cost {
    private UserFeedback _userFeedback;
    private String _playerId;
    private AwaitingDecision _decision;

    public PlayoutDecisionEffect(UserFeedback userFeedback, String playerId, AwaitingDecision decision) {
        _userFeedback = userFeedback;
        _playerId = playerId;
        _decision = decision;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        _userFeedback.sendAwaitingDecision(_playerId, _decision);
    }

    @Override
    public CostResolution playCost(LotroGame game) {
        _userFeedback.sendAwaitingDecision(_playerId, _decision);
        return new CostResolution(null, true);
    }
}
