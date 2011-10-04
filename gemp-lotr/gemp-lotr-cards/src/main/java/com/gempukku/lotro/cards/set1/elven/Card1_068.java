package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseOpponentEffect;
import com.gempukku.lotro.cards.effects.DiscardCardAtRandomFromHandEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Tale. Bearer must be an Elf companion. Archery: If bearer is an archer, exert bearer to make an opponent
 * discard 2 cards at random from hand.
 */
public class Card1_068 extends AbstractAttachable {
    public Card1_068() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 1, Culture.ELVEN, null, "The White Arrows of Lorien");
        addKeyword(Keyword.TALE);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.race(Race.ELF), Filters.type(CardType.COMPANION));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.ARCHERY, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), self.getAttachedTo())
                && game.getModifiersQuerying().hasKeyword(game.getGameState(), self.getAttachedTo(), Keyword.ARCHER)) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.ARCHERY);
            action.appendCost(new ExertCharactersEffect(self, self.getAttachedTo()));
            action.appendEffect(
                    new ChooseOpponentEffect(playerId) {
                        @Override
                        protected void opponentChosen(String opponentId) {
                            action.appendEffect(new DiscardCardAtRandomFromHandEffect(self, opponentId));
                            action.appendEffect(new DiscardCardAtRandomFromHandEffect(self, opponentId));
                        }
                    });

            return Collections.singletonList(action);
        }
        return null;
    }
}
