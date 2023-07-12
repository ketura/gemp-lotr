package com.gempukku.lotro.cards.cardtype;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.lotronly.PlayEventAction;

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
    public abstract PlayEventAction getPlayEventCardAction(String playerId, DefaultGame game, PhysicalCard self);
}
