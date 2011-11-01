package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.timing.AbstractSubActionEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;

public class ExhaustCharacterEffect extends AbstractSubActionEffect {
    private PhysicalCard _source;
    private Action _action;
    private Filterable[] _filters;

    public ExhaustCharacterEffect(PhysicalCard source, Action action, PhysicalCard physicalCard) {
        this(source, action, Filters.sameCard(physicalCard));
    }

    public ExhaustCharacterEffect(PhysicalCard source, Action action, Filterable... filters) {
        _source = source;
        _action = action;
        _filters = filters;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public String getText(LotroGame game) {
        return "Exhaust " + GameUtils.getAppendedNames(Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), _filters));
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), _filters).size() > 0;
    }

    @Override
    public Collection<? extends EffectResult> playEffect(LotroGame game) {
        SubAction subAction = new SubAction(_action);
        subAction.appendEffect(new InfiniteExertionEffect(_source, subAction, _filters));
        processSubAction(game, subAction);
        final Collection<PhysicalCard> cards = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), _filters);
        if (cards.size() > 0)
            game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " exhausts " + GameUtils.getAppendedNames(cards));

        return null;
    }

    private class InfiniteExertionEffect extends ExertCharactersEffect {
        private SubAction _subAction;

        private InfiniteExertionEffect(PhysicalCard source, SubAction subAction, Filterable[] filters) {
            super(source, filters);
            _subAction = subAction;
        }

        @Override
        protected Collection<? extends EffectResult> playoutEffectOn(LotroGame game, Collection<PhysicalCard> cards) {
            final Collection<? extends EffectResult> effectResults = super.playoutEffectOn(game, cards);
            if (getAffectedCards(game).size() > 0)
                _subAction.appendEffect(new InfiniteExertionEffect(_source, _subAction, _filters));
            return effectResults;
        }
    }
}