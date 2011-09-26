package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collection;
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
                            ActivateCardAction action = new ActivateCardAction(null, null);
                            action.appendEffect(new HealCompanion(game.getGameState().getCurrentPlayerId(), action, 1));
                            return Collections.singletonList(action);
                        }
                        return null;
                    }
                });
    }

    private class HealCompanion extends UnrespondableEffect {
        private String _fpPlayerId;
        private ActivateCardAction _action;
        private int _counter;

        private HealCompanion(String fpPlayerId, ActivateCardAction action, int counter) {
            _fpPlayerId = fpPlayerId;
            _action = action;
            _counter = counter;
        }

        @Override
        public void doPlayEffect(LotroGame game) {
            _action.appendEffect(
                    new ChooseActiveCardsEffect(_fpPlayerId, "Sanctuary healing - Choose companion to heal - remaining heals: " + (6 - _counter), 0, 1, Filters.type(CardType.COMPANION)) {
                        @Override
                        protected void cardsSelected(Collection<PhysicalCard> cards) {
                            if (cards.size() > 0) {
                                _action.appendEffect(new HealCharacterEffect(_fpPlayerId, cards.iterator().next()));
                                if (_counter < 5)
                                    _action.appendEffect(new HealCompanion(_fpPlayerId, _action, _counter + 1));
                            }
                        }
                    });
        }
    }
}
