package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

public class SanctuaryRule {
    private DefaultActionsEnvironment _actionsEnvironment;

    public SanctuaryRule(DefaultActionsEnvironment actionsEnvironment) {
        _actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        _actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
                        if (effectResult.getType() == EffectResult.Type.START_OF_TURN
                                && game.getModifiersQuerying().hasKeyword(game.getGameState(), game.getGameState().getCurrentSite(), Keyword.SANCTUARY)) {
                            DefaultCostToEffectAction action = new DefaultCostToEffectAction(null, null, "Sanctuary healing");
                            action.addEffect(new HealCompanion(game.getGameState().getCurrentPlayerId(), action, 1));
                            return Collections.singletonList(action);
                        }
                        return null;
                    }
                });
    }

    private class HealCompanion extends UnrespondableEffect {
        private String _fpPlayerId;
        private DefaultCostToEffectAction _action;
        private int _counter;

        private HealCompanion(String fpPlayerId, DefaultCostToEffectAction action, int counter) {
            _fpPlayerId = fpPlayerId;
            _action = action;
            _counter = counter;
        }

        @Override
        public void doPlayEffect(LotroGame game) {
            _action.addEffect(
                    new ChooseActiveCardsEffect(_fpPlayerId, "Sanctuary healing - Choose companion to heal", 0, 1, Filters.type(CardType.COMPANION)) {
                        @Override
                        protected void cardsSelected(List<PhysicalCard> cards) {
                            if (cards.size() > 0) {
                                _action.addEffect(new HealCharacterEffect(_fpPlayerId, cards.get(0)));
                                if (_counter < 5)
                                    _action.addEffect(new HealCompanion(_fpPlayerId, _action, _counter + 1));
                            }
                        }
                    });
        }
    }
}
