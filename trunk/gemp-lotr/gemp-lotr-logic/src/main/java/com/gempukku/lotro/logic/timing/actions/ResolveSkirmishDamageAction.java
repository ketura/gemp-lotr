package com.gempukku.lotro.logic.timing.actions;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.results.NormalSkirmishResult;

import java.util.List;

public class ResolveSkirmishDamageAction extends RequiredTriggerAction {
    private NormalSkirmishResult _skirmishResult;

    private Integer _damageToDo;
    private int _remainingDamage;

    public ResolveSkirmishDamageAction(NormalSkirmishResult skirmishResult) {
        super(null);
        _skirmishResult = skirmishResult;
    }

    @Override
    public String getText(LotroGame game) {
        return "Resolving skirmish";
    }

    @Override
    public Effect nextEffect(LotroGame game) {
        if (_damageToDo == null) {
            _damageToDo = calculateDamageToDo(game);
            _remainingDamage = _damageToDo;
        }

        if (_remainingDamage > 0) {
            _remainingDamage--;
            return new WoundCharactersEffect(_skirmishResult.getWinners(), Filters.in(_skirmishResult.getLosers()));
        }

        return null;
    }

    public void consumeRemainingDamage() {
        _remainingDamage = 0;
    }

    public int getRemainingDamage() {
        return _remainingDamage;
    }

    private int calculateDamageToDo(LotroGame game) {
        List<PhysicalCard> winners = _skirmishResult.getWinners();
        int dmg = 1;
        ModifiersQuerying modifiersQuerying = game.getModifiersQuerying();
        GameState gameState = game.getGameState();
        for (PhysicalCard winner : winners)
            dmg += modifiersQuerying.getKeywordCount(gameState, winner, Keyword.DAMAGE);

        return dmg;
    }
}
