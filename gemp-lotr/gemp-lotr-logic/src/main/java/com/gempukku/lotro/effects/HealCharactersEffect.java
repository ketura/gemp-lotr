package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.rules.GameUtils;
import com.gempukku.lotro.effects.results.HealResult;

import java.util.Collection;

public class HealCharactersEffect extends AbstractPreventableCardEffect {
    private final LotroPhysicalCard _source;
    private final String _performingPlayer;

    public HealCharactersEffect(LotroPhysicalCard source, String performingPlayer, LotroPhysicalCard... cards) {
        super(cards);
        _source = source;
        _performingPlayer = performingPlayer;
    }

    public HealCharactersEffect(LotroPhysicalCard source, String performingPlayer, Filterable... filter) {
        super(filter);
        _source = source;
        _performingPlayer = performingPlayer;
    }

    @Override
    protected Filter getExtraAffectableFilter() {
        return Filters.and(
                Filters.wounded,
                new Filter() {
                    @Override
                    public boolean accepts(DefaultGame game, LotroPhysicalCard physicalCard) {
                        return game.getModifiersQuerying().canBeHealed(game, physicalCard);
                    }
                });
    }

    @Override
    public Effect.Type getType() {
        return Type.BEFORE_HEALED;
    }

    @Override
    public String getText(DefaultGame game) {
        Collection<LotroPhysicalCard> cards = getAffectedCardsMinusPrevented(game);
        return "Heal - " + getAppendedTextNames(cards);
    }

    @Override
    protected void playoutEffectOn(DefaultGame game, Collection<LotroPhysicalCard> cards) {
        Collection<LotroPhysicalCard> cardsToHeal = getAffectedCardsMinusPrevented(game);

        if (cardsToHeal.size() > 0) {
            game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " heals " + getAppendedNames(cardsToHeal));
            for (LotroPhysicalCard cardToHeal : cardsToHeal) {
                game.getGameState().removeWound(cardToHeal);
                game.getActionsEnvironment().emitEffectResult(new HealResult(cardToHeal, _source, _performingPlayer));
            }
        }
    }
}
