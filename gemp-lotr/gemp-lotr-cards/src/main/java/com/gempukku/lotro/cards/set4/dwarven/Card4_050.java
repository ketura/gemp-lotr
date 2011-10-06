package com.gempukku.lotro.cards.set4.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Tale.Plays to your support area.
 * Skirmish: Stack a Free Peoples card from hand here to make a Dwarf damage +1.
 */
public class Card4_050 extends AbstractPermanent {
    public Card4_050() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "Here Is Good Rock", true);
        addKeyword(Keyword.TALE);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.side(Side.FREE_PEOPLE)).size() > 0) {
            ActivateCardAction action = new ActivateCardAction(self, Keyword.SKIRMISH);
            action.appendCost(
                    new ChooseCardsFromHandEffect() {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                        }
                    }
            );
        }
    }
}
