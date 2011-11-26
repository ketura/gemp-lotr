package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.KillEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;
import com.gempukku.lotro.logic.timing.results.ZeroVitalityResult;

import java.util.*;

public class CharacterDeathRule {
    private Set<PhysicalCard> _charactersAlreadyOnWayToDeath = new HashSet<PhysicalCard>();
    private DefaultActionsEnvironment _actionsEnvironment;

    public CharacterDeathRule(DefaultActionsEnvironment actionsEnvironment) {
        _actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        _actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
                        if (effectResult.getType() == EffectResult.Type.ZERO_VITALITY) {
                            ZeroVitalityResult zeroVitalityResult = (ZeroVitalityResult) effectResult;
                            final PhysicalCard deadCharacter = zeroVitalityResult.getCharacter();
                            RequiredTriggerAction action = new RequiredTriggerAction(deadCharacter);
                            action.setMessage(null);
                            action.setText("Character death");
                            action.appendEffect(
                                    new KillEffect(Collections.singletonList(deadCharacter), KillEffect.Cause.WOUNDS));
                            action.appendEffect(
                                    new UnrespondableEffect() {
                                        @Override
                                        protected void doPlayEffect(LotroGame game) {
                                            _charactersAlreadyOnWayToDeath.remove(deadCharacter);
                                        }
                                    });

                            return Collections.singletonList(action);
                        }
                        return null;
                    }
                });
    }

    public void checkCharactersZeroVitality(LotroGame game) {
        if (game.getGameState().getCurrentPhase() != Phase.PUT_RING_BEARER) {
            GameState gameState = game.getGameState();

            Collection<PhysicalCard> characters = Filters.filterActive(gameState, game.getModifiersQuerying(),
                    Filters.or(CardType.ALLY, CardType.COMPANION, CardType.MINION));

            for (PhysicalCard character : characters)
                if (!_charactersAlreadyOnWayToDeath.contains(character) && game.getModifiersQuerying().getVitality(gameState, character) <= 0) {
                    _charactersAlreadyOnWayToDeath.add(character);
                    game.getActionsEnvironment().emitEffectResult(new ZeroVitalityResult(character));
                }
        }
    }
}
