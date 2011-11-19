package com.gempukku.lotro.logic.timing.actions;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ResolveSkirmishEffect;
import com.gempukku.lotro.logic.effects.TriggeringResultEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.results.SkirmishAboutToEndResult;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ResolveSkirmishAction implements Action {
    private Iterator<Effect> _effects;

    @Override
    public void setVirtualCardAction(boolean virtualCardAction) {
    }

    @Override
    public boolean isVirtualCardAction() {
        return false;
    }

    @Override
    public Phase getActionTimeword() {
        return null;
    }

    @Override
    public void setActionTimeword(Phase phase) {
    }

    @Override
    public String getPerformingPlayer() {
        return null;
    }

    @Override
    public void setPerformingPlayer(String playerId) {
    }

    @Override
    public PhysicalCard getActionSource() {
        return null;
    }

    @Override
    public PhysicalCard getActionAttachedToCard() {
        return null;
    }

    @Override
    public String getText(LotroGame game) {
        return "Resolving skirmish";
    }

    @Override
    public Effect nextEffect(LotroGame game) {
        if (_effects == null) {
            _effects = resolveSkirmish();
        }

        if (_effects.hasNext())
            return _effects.next();
        else
            return null;
    }

    private Iterator<Effect> resolveSkirmish() {
        List<Effect> effects = new LinkedList<Effect>();

        effects.add(new ResolveSkirmishEffect());

        effects.add(new TriggeringResultEffect(new SkirmishAboutToEndResult(), "Skirmish about to end"));

        return effects.iterator();
    }
}
