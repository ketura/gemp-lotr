package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.KillEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CharacterDeathRule {
    private DefaultActionsEnvironment _actionsEnvironment;

    public CharacterDeathRule(DefaultActionsEnvironment actionsEnvironment) {
        _actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        _actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
                        GameState gameState = game.getGameState();

                        List<PhysicalCard> deadCharacters = new LinkedList<PhysicalCard>();

                        Collection<PhysicalCard> characters = Filters.filterActive(gameState, game.getModifiersQuerying(),
                                Filters.or(Filters.type(CardType.ALLY), Filters.type(CardType.COMPANION), Filters.type(CardType.MINION)));
                        for (PhysicalCard character : characters)
                            if (game.getModifiersQuerying().getVitality(gameState, character) <= 0)
                                deadCharacters.add(character);

                        if (deadCharacters.size() > 0) {
                            RequiredTriggerAction action = new RequiredTriggerAction(null) {
                                @Override
                                public String getText(LotroGame game) {
                                    return "Character(s) death";
                                }
                            };
                            action.appendEffect(
                                    new KillEffect(deadCharacters));
                            return Collections.singletonList(action);
                        }

                        return null;
                    }
                }
        );
    }
}
