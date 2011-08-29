package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;
import com.gempukku.lotro.logic.timing.rules.CharacterDeathRule;
import com.gempukku.lotro.logic.timing.rules.EndEffectsAndActionsRule;
import com.gempukku.lotro.logic.timing.rules.ResolveSkirmishRule;
import com.gempukku.lotro.logic.timing.rules.RoamingRule;

public class RuleSet {
    private LotroGame _game;
    private DefaultActionsEnvironment _actionsEnvironment;
    private ModifiersLogic _modifiersLogic;

    public RuleSet(LotroGame game, DefaultActionsEnvironment actionsEnvironment, ModifiersLogic modifiersLogic) {
        _game = game;
        _actionsEnvironment = actionsEnvironment;
        _modifiersLogic = modifiersLogic;
    }

    public void applyRuleSet() {

        new EndEffectsAndActionsRule(_actionsEnvironment, _modifiersLogic).applyRule();

        new RoamingRule(_modifiersLogic).applyRule();

        new ResolveSkirmishRule(_game, _actionsEnvironment).applyRule();

        new CharacterDeathRule(_actionsEnvironment).applyRule();
    }
}
