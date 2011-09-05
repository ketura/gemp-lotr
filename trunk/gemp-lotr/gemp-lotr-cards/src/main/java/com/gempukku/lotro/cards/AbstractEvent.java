package com.gempukku.lotro.cards;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

public abstract class AbstractEvent extends AbstractLotroCardBlueprint {
    private Phase _playableInPhase;

    public AbstractEvent(Side side, Culture culture, String name, Phase playableInPhase) {
        super(side, CardType.EVENT, culture, name);
        _playableInPhase = playableInPhase;
        if (_playableInPhase == Phase.FELLOWSHIP)
            addKeyword(Keyword.FELLOWSHIP);
        else if (_playableInPhase == Phase.SHADOW)
            addKeyword(Keyword.SHADOW);
        else if (_playableInPhase == Phase.MANEUVER)
            addKeyword(Keyword.MANEUVER);
        else if (_playableInPhase == Phase.ARCHERY)
            addKeyword(Keyword.ARCHERY);
        else if (_playableInPhase == Phase.ASSIGNMENT)
            addKeyword(Keyword.ASSIGNMENT);
        else if (_playableInPhase == Phase.SKIRMISH)
            addKeyword(Keyword.SKIRMISH);
        else if (_playableInPhase == Phase.REGROUP)
            addKeyword(Keyword.REGROUP);
        else
            addKeyword(Keyword.RESPONSE);
    }

    @Override
    public final List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (_playableInPhase != null) {
            Side side = self.getBlueprint().getSide();
            if ((side == Side.FREE_PEOPLE && PlayConditions.canPlayFPCardDuringPhase(game, _playableInPhase, self))
                    || (side == Side.SHADOW && PlayConditions.canPlayShadowCardDuringPhase(game, _playableInPhase, self))) {
                if (checkPlayRequirements(playerId, game, self))
                    return Collections.singletonList(getPlayCardAction(playerId, game, self, 0));
            }
        }
        return null;
    }
}
