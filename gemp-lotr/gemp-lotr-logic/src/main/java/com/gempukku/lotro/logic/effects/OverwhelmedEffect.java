package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractSuccessfulEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.OverwhelmSkirmishResult;

import java.util.List;

public class OverwhelmedEffect extends AbstractSuccessfulEffect {
    private List<PhysicalCard> _winners;
    private List<PhysicalCard> _losers;

    public OverwhelmedEffect(List<PhysicalCard> winners, List<PhysicalCard> losers) {
        _winners = winners;
        _losers = losers;
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.OVERWHELM_IN_SKIRMISH;
    }

    @Override
    public String getText(LotroGame game) {
        return getAppendedNames(_losers) + " " + GameUtils.be(_losers) + " overwhelmed in skirmish";
    }

    @Override
    public EffectResult[] playEffect(LotroGame game) {
        game.getGameState().sendMessage("Skirmish finishes with an overwhelm");
        return new EffectResult[]{new OverwhelmSkirmishResult(_winners, _losers)};
    }
}
