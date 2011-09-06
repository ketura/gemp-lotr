package com.gempukku.lotro.cards;

import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

public abstract class AbstractEvent extends AbstractLotroCardBlueprint {
    private Phase[] _playableInPhases;

    public AbstractEvent(Side side, Culture culture, String name, Phase... playableInPhases) {
        super(side, CardType.EVENT, culture, name);
        _playableInPhases = playableInPhases;
        for (Phase playableInPhase : _playableInPhases)
            processPhase(playableInPhase);
    }

    private void processPhase(Phase phase) {
        if (phase == Phase.FELLOWSHIP)
            addKeyword(Keyword.FELLOWSHIP);
        else if (phase == Phase.SHADOW)
            addKeyword(Keyword.SHADOW);
        else if (phase == Phase.MANEUVER)
            addKeyword(Keyword.MANEUVER);
        else if (phase == Phase.ARCHERY)
            addKeyword(Keyword.ARCHERY);
        else if (phase == Phase.ASSIGNMENT)
            addKeyword(Keyword.ASSIGNMENT);
        else if (phase == Phase.SKIRMISH)
            addKeyword(Keyword.SKIRMISH);
        else if (phase == Phase.REGROUP)
            addKeyword(Keyword.REGROUP);
        else
            addKeyword(Keyword.RESPONSE);
    }

    @Override
    public final List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (_playableInPhases != null) {
            Side side = self.getBlueprint().getSide();
            if ((side == Side.FREE_PEOPLE && PlayConditions.canPlayFPCardDuringPhase(game, _playableInPhases, self))
                    || (side == Side.SHADOW && PlayConditions.canPlayShadowCardDuringPhase(game, _playableInPhases, self))) {
                if (checkPlayRequirements(playerId, game, self, 0))
                    return Collections.singletonList(getPlayCardAction(playerId, game, self, 0));
            }
        }
        return null;
    }

    @Override
    public abstract PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier);
}
