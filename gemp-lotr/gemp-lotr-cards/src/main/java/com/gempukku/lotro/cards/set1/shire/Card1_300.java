package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Possession
 * Game Text: Pipeweed. Plays to your support area. When you play this possession, you may discard up to 2 cards from
 * hand.
 */
public class Card1_300 extends AbstractPermanent {
    public Card1_300() {
        super(Side.FREE_PEOPLE, 1, CardType.POSSESSION, Culture.SHIRE, Zone.SUPPORT, "Longbottom Leaf");
        addKeyword(Keyword.PIPEWEED);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.sameCard(self))) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseArbitraryCardsEffect(playerId, "Choose up to 2 cards to discard from hand", game.getGameState().getHand(playerId), 0, 2) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            action.appendEffect(new DiscardCardsFromHandEffect(self, playerId, selectedCards, false));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
