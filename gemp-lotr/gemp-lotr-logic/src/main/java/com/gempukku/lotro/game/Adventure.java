package com.gempukku.lotro.game;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;

public interface Adventure {
    public void applyAdventureRules(LotroGame game, DefaultActionsEnvironment actionsEnvironment, ModifiersLogic modifiersLogic);
}
