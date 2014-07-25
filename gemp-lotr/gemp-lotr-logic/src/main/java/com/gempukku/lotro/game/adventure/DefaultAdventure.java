package com.gempukku.lotro.game.adventure;

import com.gempukku.lotro.game.Adventure;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;
import com.gempukku.lotro.logic.timing.rules.WinConditionRule;

public class DefaultAdventure implements Adventure {
    @Override
    public void applyAdventureRules(LotroGame game, DefaultActionsEnvironment actionsEnvironment, ModifiersLogic modifiersLogic) {
        new WinConditionRule(actionsEnvironment).applyRule();
    }
}
