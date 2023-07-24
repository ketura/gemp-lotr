package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.rules.GameUtils;
import com.gempukku.lotro.game.actions.Action;
import com.gempukku.lotro.game.timing.results.ExertResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ExertCharactersEffect extends AbstractPreventableCardEffect {
    private final Action _action;
    private final LotroPhysicalCard _source;
    private final Set<LotroPhysicalCard> _placeNoWound = new HashSet<>();
    private boolean _forToil;

    public ExertCharactersEffect(Action action, LotroPhysicalCard source, Filterable... filter) {
        super(filter);
        _action = action;
        _source = source;
    }

    public ExertCharactersEffect(Action action, LotroPhysicalCard source, LotroPhysicalCard... cards) {
        super(cards);
        _action = action;
        _source = source;
    }

    public void setForToil(boolean forToil) {
        _forToil = forToil;
    }

    @Override
    protected Filter getExtraAffectableFilter() {
        return new Filter() {
            @Override
            public boolean accepts(DefaultGame game, LotroPhysicalCard physicalCard) {
                return game.getModifiersQuerying().canBeExerted(game, _source, physicalCard)
                        && game.getModifiersQuerying().getVitality(game, physicalCard) > 1;
            }
        };
    }

    @Override
    public Effect.Type getType() {
        return Type.BEFORE_EXERT;
    }

    @Override
    public String getText(DefaultGame game) {
        Collection<LotroPhysicalCard> cards = getAffectedCardsMinusPrevented(game);
        return "Exert - " + getAppendedTextNames(cards);
    }

    @Override
    protected void playoutEffectOn(DefaultGame game, Collection<LotroPhysicalCard> cards) {
        if (cards.size() > 0)
            game.getGameState().sendMessage(getAppendedNames(cards) + " exert" + GameUtils.s(cards) + " due to " + GameUtils.getCardLink(_source));

        for (LotroPhysicalCard woundedCard : cards) {
            if (!_placeNoWound.contains(woundedCard))
                game.getGameState().addWound(woundedCard);
            game.getActionsEnvironment().emitEffectResult(new ExertResult(_action, woundedCard, _forToil));
            forEachExertedByEffect(woundedCard);
        }
    }

    protected void forEachExertedByEffect(LotroPhysicalCard physicalCard) {

    }

    public void placeNoWoundOn(LotroPhysicalCard physicalCard) {
        _placeNoWound.add(physicalCard);
    }
}
