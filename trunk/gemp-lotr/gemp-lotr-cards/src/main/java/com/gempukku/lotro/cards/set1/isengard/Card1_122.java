package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.PutCardFromDiscardOnBottomOfDeckEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Response: If you play an Uruk-hai, take all copies of that card in your discard pile and place them
 * beneath your draw deck.
 */
public class Card1_122 extends AbstractLotroCardBlueprint {
    public Card1_122() {
        super(Side.SHADOW, CardType.EVENT, Culture.ISENGARD, "Breeding Pit", "1_122");
        addKeyword(Keyword.RESPONSE);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public List<? extends Action> getPlayableWhenActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.and(Filters.keyword(Keyword.URUK_HAI), Filters.owner(playerId)))
                && PlayConditions.canPlayShadowCardDuringPhase(game.getGameState(), game.getModifiersQuerying(), null, self)) {
            final PlayEventAction action = new PlayEventAction(self);
            String playedCardName = ((PlayCardResult) effectResult).getPlayedCard().getBlueprint().getName();
            List<PhysicalCard> cardsInDiscardWithSameName = Filters.filter(game.getGameState().getDiscard(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.name(playedCardName));
            for (PhysicalCard physicalCard : cardsInDiscardWithSameName)
                action.addEffect(new PutCardFromDiscardOnBottomOfDeckEffect(physicalCard));

            return Collections.singletonList(action);
        }
        return null;
    }
}
