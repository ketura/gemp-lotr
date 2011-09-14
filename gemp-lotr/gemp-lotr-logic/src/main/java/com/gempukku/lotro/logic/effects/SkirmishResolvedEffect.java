package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.NormalSkirmishResult;

import java.util.List;

public class SkirmishResolvedEffect extends AbstractEffect {
    private List<PhysicalCard> _winners;
    private List<PhysicalCard> _losers;

    public SkirmishResolvedEffect(List<PhysicalCard> winners, List<PhysicalCard> losers) {
        _winners = winners;
        _losers = losers;
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.RESOLVE_SKIRMISH;
    }

    @Override
    public String getText() {
        return "Character(s) won skirmish";
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return true;
    }

    @Override
    public EffectResult playEffect(LotroGame game) {
        game.getGameState().sendMessage("Skirmish finishes with a normal win");
        return new NormalSkirmishResult(_winners, _losers);
    }
}
