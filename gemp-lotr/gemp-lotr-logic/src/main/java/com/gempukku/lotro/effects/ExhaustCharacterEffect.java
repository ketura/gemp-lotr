package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.rules.GameUtils;
import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.actions.lotronly.SubAction;
import com.gempukku.lotro.actions.Action;

import java.util.Collection;

public class ExhaustCharacterEffect extends AbstractSubActionEffect {
    private final LotroPhysicalCard _source;
    private final Action _action;
    private final Filterable[] _filters;

    public ExhaustCharacterEffect(LotroPhysicalCard source, Action action, LotroPhysicalCard physicalCard) {
        this(source, action, Filters.sameCard(physicalCard));
    }

    public ExhaustCharacterEffect(LotroPhysicalCard source, Action action, Filterable... filters) {
        _source = source;
        _action = action;
        _filters = filters;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Exhaust " + GameUtils.getAppendedNames(Filters.filterActive(game, _filters));
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return Filters.filterActive(game, _filters).size() > 0;
    }

    @Override
    public void playEffect(DefaultGame game) {
        SubAction subAction = new SubAction(_action);
        subAction.appendEffect(new InfiniteExertionEffect(_source, subAction, _filters));
        processSubAction(game, subAction);
        final Collection<LotroPhysicalCard> cards = Filters.filterActive(game, _filters);
        if (cards.size() > 0)
            game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " exhausts " + GameUtils.getAppendedNames(cards));
    }

    private class InfiniteExertionEffect extends ExertCharactersEffect {
        private final CostToEffectAction _subAction;

        private InfiniteExertionEffect(LotroPhysicalCard source, CostToEffectAction subAction, Filterable[] filters) {
            super(_action, source, filters);
            _subAction = subAction;
        }

        @Override
        protected void playoutEffectOn(DefaultGame game, Collection<LotroPhysicalCard> cards) {
            super.playoutEffectOn(game, cards);
            if (getAffectedCards(game).size() > 0)
                _subAction.appendEffect(new InfiniteExertionEffect(_source, _subAction, _filters));
        }
    }
}