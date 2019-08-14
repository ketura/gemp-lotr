package com.gempukku.lotro.logic.cardtype;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;

public abstract class AbstractEvent extends AbstractLotroCardBlueprint {
    public AbstractEvent(Side side, int twilightCost, Culture culture, String name, Phase playableInPhase, Phase... additionalPlayableInPhases) {
        super(twilightCost, side, CardType.EVENT, culture, name);
        if (playableInPhase != null) {
            addPhaseKeyword(playableInPhase);
            for (Phase additionalPlayableInPhase : additionalPlayableInPhases)
                addPhaseKeyword(additionalPlayableInPhase);
        }
    }

    private void addPhaseKeyword(Phase phase) {
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
    }

    @Override
    public abstract PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier);
}
