package com.gempukku.lotro.cards.effects.choose;

import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.StackCardFromPlayEffect;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.AbstractSubActionEffect;
import com.gempukku.lotro.logic.timing.Action;

public class ChooseAndStackCardFromPlayEffect extends AbstractSubActionEffect {
    private Action _action;
    private String _playerId;
    private Filterable _stackOnFilter;
    private Filterable[] _cardFilter;

    public ChooseAndStackCardFromPlayEffect(Action action, String playerId, Filterable stackOn, Filterable... filter) {
        _action = action;
        _playerId = playerId;
        _stackOnFilter = stackOn;
        _cardFilter = filter;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return PlayConditions.isActive(game, _cardFilter) && PlayConditions.isActive(game, _stackOnFilter);
    }

    @Override
    public void playEffect(LotroGame game) {
        final SubAction subAction = new SubAction(_action);
        subAction.appendEffect(
                new ChooseActiveCardEffect(_action.getActionSource(), _playerId, "Choose card to stack", _cardFilter) {
                    @Override
                    protected void cardSelected(LotroGame game, final PhysicalCard cardToStack) {
                        subAction.appendEffect(
                                new ChooseActiveCardEffect(_action.getActionSource(), _playerId, "Choose card to stack on", _stackOnFilter) {
                                    @Override
                                    protected void cardSelected(LotroGame game, PhysicalCard cardToStackOn) {
                                        subAction.appendEffect(
                                                new StackCardFromPlayEffect(cardToStack, cardToStackOn));
                                    }
                                });
                    }
                });
        processSubAction(game, subAction);
    }
}