package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. Response: If your [MORIA] weapon is discarded, play it from your discard pile
 * (that weapon's twilight cost is -1).
 */
public class Card1_193 extends AbstractPermanent {
    public Card1_193() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.MORIA, Zone.SUPPORT, "Plundered Armories");
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.DISCARD_FROM_PLAY) {
            DiscardCardsFromPlayResult discardResult = (DiscardCardsFromPlayResult) effectResult;
            Collection<PhysicalCard> discardedCards = discardResult.getDiscardedCards();
            if (Filters.filter(discardedCards, game.getGameState(), game.getModifiersQuerying(), Filters.zone(Zone.DISCARD), Filters.culture(Culture.MORIA), Filters.or(Filters.keyword(Keyword.HAND_WEAPON), Filters.keyword(Keyword.RANGED_WEAPON)), Filters.playable(game, -1)).size() > 0) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.appendEffect(
                        new ChooseAndPlayCardFromDiscardEffect(playerId,
                                game.getGameState().getDiscard(playerId),
                                Filters.and(
                                        Filters.culture(Culture.MORIA),
                                        Filters.or(Filters.keyword(Keyword.HAND_WEAPON), Filters.keyword(Keyword.RANGED_WEAPON)),
                                        Filters.in(discardedCards)), -1));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
