package com.gempukku.lotro.cards;

import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

public abstract class AbstractOldEvent extends AbstractLotroCardBlueprint {
    private Phase[] _playableInPhases;

    public AbstractOldEvent(Side side, Culture culture, String name, Phase... playableInPhases) {
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
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return (getSide() != Side.SHADOW
                || PlayConditions.canPayForShadowCard(game, self, twilightModifier));
    }

    @Override
    public final List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (_playableInPhases != null) {
            if (PlayConditions.canPlayCardDuringPhase(game, _playableInPhases, self)) {
                if (checkPlayRequirements(playerId, game, self, 0))
                    return Collections.singletonList(getPlayCardAction(playerId, game, self, 0));
            }
        }
        return null;
    }

    @Override
    public abstract PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier);

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        return null;
    }

    @Override
    public List<PlayEventAction> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        return null;
    }

    @Override
    public final List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final List<OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, LotroGame lotroGame, Effect effect, PhysicalCard self) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final List<RequiredTriggerAction> getRequiredBeforeTriggers(LotroGame game, Effect effect, PhysicalCard self) {
        throw new UnsupportedOperationException();
    }
}
