package com.gempukku.lotro.cards.set1.gandalf;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.RevealAndChooseCardsFromOpponentHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be Gandalf. He is damage +1. Fellowship or Regroup: Exert Gandalf to reveal an opponent's
 * hand. Remove (1) for each Orc revealed.
 */
public class Card1_075 extends AbstractAttachableFPPossession {
    public Card1_075() {
        super(2, 2, 0, Culture.GANDALF, Keyword.HAND_WEAPON, "Glamdring", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Gandalf");
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(new KeywordModifier(self, Filters.hasAttached(self), Keyword.DAMAGE));
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if ((PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                || PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.REGROUP, self))
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), self.getAttachedTo())) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new ExertCharactersEffect(self, self.getAttachedTo()));
            action.appendEffect(
                    new ChooseOpponentEffect(playerId) {
                        @Override
                        protected void opponentChosen(final String opponentId) {
                            action.appendEffect(
                                    new RevealAndChooseCardsFromOpponentHandEffect(playerId, opponentId, "Opponent's hand", Filters.none(), 0, 0) {
                                        @Override
                                        protected void cardsSelected(List<PhysicalCard> selectedCards) {
                                            Collection<PhysicalCard> orcs = Filters.filter(game.getGameState().getHand(opponentId), game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.ORC));
                                            action.appendEffect(new RemoveTwilightEffect(orcs.size()));
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
