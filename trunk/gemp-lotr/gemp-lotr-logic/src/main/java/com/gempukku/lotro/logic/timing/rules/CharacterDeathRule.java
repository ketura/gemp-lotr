package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.KillEffect;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CharacterDeathRule {
    public List<RequiredTriggerAction> getKillEffects(LotroGame game) {
        GameState gameState = game.getGameState();

        List<PhysicalCard> deadCharacters = new LinkedList<PhysicalCard>();

        Collection<PhysicalCard> characters = Filters.filterActive(gameState, game.getModifiersQuerying(),
                Filters.or(Filters.type(CardType.ALLY), Filters.type(CardType.COMPANION), Filters.type(CardType.MINION)));
        for (PhysicalCard character : characters)
            if (game.getModifiersQuerying().getVitality(gameState, character) <= 0)
                deadCharacters.add(character);

        if (deadCharacters.size() > 0) {
            RequiredTriggerAction action = new RequiredTriggerAction(null);
            action.setText("Character(s) death");
            action.appendEffect(
                    new KillEffect(deadCharacters));
            return Collections.singletonList(action);
        }

        return null;
    }
}
