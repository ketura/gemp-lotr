package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.DiscardCardFromHandEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

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
        super(Side.FREE_PEOPLE, 1, CardType.POSSESSION, Culture.SHIRE, Zone.FREE_SUPPORT, "Longbottom Leaf");
        addKeyword(Keyword.PIPEWEED);
    }

    @Override
    public List<? extends Action> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.sameCard(self))) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "You may discard up to 2 cards from hand.");
            action.addEffect(
                    new ChooseCardsFromHandEffect(playerId, "Choose up to 2 cards to discard from hand", 0, 2, Filters.any()) {
                        @Override
                        protected void cardsSelected(List<PhysicalCard> selectedCards) {
                            for (PhysicalCard selectedCard : selectedCards)
                                action.addEffect(new DiscardCardFromHandEffect(selectedCard));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
