package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.rules.GameUtils;
import com.gempukku.lotro.game.modifiers.ModifierFlag;
import com.gempukku.lotro.game.timing.results.WoundResult;

import java.util.Collection;
import java.util.Collections;

public class WoundCharactersEffect extends AbstractPreventableCardEffect {
    private Collection<LotroPhysicalCard> _sources = Collections.emptySet();
    private String _sourceText;

    public WoundCharactersEffect(Collection<LotroPhysicalCard> sources, Filterable... filter) {
        super(filter);
        _sources = sources;
        if (sources != null)
            _sourceText = GameUtils.getAppendedNames(sources);
    }

    public WoundCharactersEffect(LotroPhysicalCard source, LotroPhysicalCard... cards) {
        super(cards);
        if (source != null) {
            _sources = Collections.singleton(source);
            _sourceText = GameUtils.getCardLink(source);
        }
    }

    public WoundCharactersEffect(LotroPhysicalCard source, Filterable... filter) {
        super(filter);
        if (source != null) {
            _sources = Collections.singleton(source);
            _sourceText = GameUtils.getCardLink(source);
        }
    }

    public void setSourceText(String sourceText) {
        _sourceText = sourceText;
    }

    public Collection<LotroPhysicalCard> getSources() {
        return _sources;
    }

    @Override
    protected Filter getExtraAffectableFilter() {
        return new Filter() {
            @Override
            public boolean accepts(DefaultGame game, LotroPhysicalCard physicalCard) {
                return game.getModifiersQuerying().canTakeWounds(game, _sources, physicalCard, 1);
            }
        };
    }

    @Override
    public Effect.Type getType() {
        return Type.BEFORE_WOUND;
    }

    @Override
    public String getText(DefaultGame game) {
        Collection<LotroPhysicalCard> cards = getAffectedCardsMinusPrevented(game);
        return "Wound " + getAppendedTextNames(cards);
    }

    @Override
    protected void playoutEffectOn(DefaultGame game, Collection<LotroPhysicalCard> cards) {
        if (cards.size() > 0)
            game.getGameState().sendMessage(getAppendedNames(cards) + " " + GameUtils.be(cards) + " wounded by " + _sourceText);

        for (LotroPhysicalCard woundedCard : cards) {
            game.getGameState().addWound(woundedCard);
            game.getModifiersEnvironment().addedWound(woundedCard);
            game.getActionsEnvironment().emitEffectResult(new WoundResult(_sources, woundedCard));
        }
    }

    @Override
    public void preventEffect(DefaultGame game, LotroPhysicalCard card) {
        if (!game.getModifiersQuerying().hasFlagActive(game, ModifierFlag.CANT_PREVENT_WOUNDS))
            super.preventEffect(game, card);
    }

    public void negateWound(DefaultGame game, LotroPhysicalCard card) {
        super.preventEffect(game, card);
    }
}
