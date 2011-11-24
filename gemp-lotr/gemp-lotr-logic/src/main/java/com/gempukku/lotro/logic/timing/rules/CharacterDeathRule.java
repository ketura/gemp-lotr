package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.KillEffect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.*;

public class CharacterDeathRule {
    private Set<PhysicalCard> _charactersAlreadyOnWayToDeath = new HashSet<PhysicalCard>();

    public List<RequiredTriggerAction> getKillEffects(LotroGame game) {
        GameState gameState = game.getGameState();

        List<PhysicalCard> deadCharacters = new LinkedList<PhysicalCard>();

        Collection<PhysicalCard> characters = Filters.filterActive(gameState, game.getModifiersQuerying(),
                Filters.or(CardType.ALLY, CardType.COMPANION, CardType.MINION));
        for (PhysicalCard character : characters)
            if (!_charactersAlreadyOnWayToDeath.contains(character) && game.getModifiersQuerying().getVitality(gameState, character) <= 0)
                deadCharacters.add(character);


        if (deadCharacters.size() > 0) {
            _charactersAlreadyOnWayToDeath.addAll(deadCharacters);
            return Collections.singletonList((RequiredTriggerAction) new KillCharactersFromVitalityAction(deadCharacters));
        }

        return null;
    }

    private class KillCharactersFromVitalityAction extends RequiredTriggerAction {
        private KillCharactersFromVitalityAction(final List<PhysicalCard> deadCharacters) {
            super(null);
            setText("Character(s) death");
            appendEffect(
                    new KillEffect(deadCharacters, KillEffect.Cause.WOUNDS));
            appendEffect(
                    new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            _charactersAlreadyOnWayToDeath.removeAll(deadCharacters);
                        }
                    });
        }
    }
}
