package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.WoundResult;

import java.util.Collection;
import java.util.Collections;

public class WoundCharactersEffect extends AbstractPreventableCardEffect {
    private Collection<PhysicalCard> _sources;
    private String _sourceText;

    public WoundCharactersEffect(Collection<PhysicalCard> sources, Filterable... filter) {
        super(filter);
        _sources = sources;
        if (sources != null)
            _sourceText = GameUtils.getAppendedNames(sources);
    }

    public WoundCharactersEffect(PhysicalCard source, PhysicalCard... cards) {
        super(cards);
        if (source != null) {
            _sources = Collections.singleton(source);
            _sourceText = GameUtils.getCardLink(source);
        }
    }

    public WoundCharactersEffect(PhysicalCard source, Filterable... filter) {
        super(filter);
        if (source != null) {
            _sources = Collections.singleton(source);
            _sourceText = GameUtils.getCardLink(source);
        }
    }

    public void setSourceText(String sourceText) {
        _sourceText = sourceText;
    }

    public Collection<PhysicalCard> getSources() {
        return _sources;
    }

    @Override
    protected Filter getExtraAffectableFilter() {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return modifiersQuerying.canTakeWound(gameState, physicalCard);
            }
        };
    }

    @Override
    public Effect.Type getType() {
        return Type.BEFORE_WOUND;
    }

    @Override
    public String getText(LotroGame game) {
        Collection<PhysicalCard> cards = getAffectedCardsMinusPrevented(game);
        return "Wound " + getAppendedTextNames(cards);
    }

    @Override
    protected Collection<? extends EffectResult> playoutEffectOn(LotroGame game, Collection<PhysicalCard> cards) {
        if (cards.size() > 0)
            game.getGameState().sendMessage(getAppendedNames(cards) + " " + GameUtils.be(cards) + " wounded by " + _sourceText);

        for (PhysicalCard woundedCard : cards) {
            game.getGameState().addWound(woundedCard);
            game.getModifiersEnvironment().addedWound(woundedCard);
        }

        return Collections.singleton(new WoundResult(cards));
    }

    @Override
    public void preventEffect(LotroGame game, PhysicalCard card) {
        if (!game.getModifiersQuerying().hasFlagActive(game.getGameState(), ModifierFlag.CANT_PREVENT_WOUNDS))
            super.preventEffect(game, card);
    }
}
