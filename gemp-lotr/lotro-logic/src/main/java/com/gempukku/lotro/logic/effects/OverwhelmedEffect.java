package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.OverwhelmSkirmishResult;

import java.util.List;

public class OverwhelmedEffect extends AbstractEffect {
    private List<PhysicalCard> _winners;
    private List<PhysicalCard> _losers;

    public OverwhelmedEffect(List<PhysicalCard> winners, List<PhysicalCard> losers) {
        _winners = winners;
        _losers = losers;
    }

    @Override
    public EffectResult getRespondableResult() {
        return new OverwhelmSkirmishResult(_winners, _losers);
    }

    @Override
    public String getText() {
        return "Character(s) was/where overwhelmed in Skirmish";
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return true;
    }

    @Override
    public void playEffect(LotroGame game) {

    }
}
