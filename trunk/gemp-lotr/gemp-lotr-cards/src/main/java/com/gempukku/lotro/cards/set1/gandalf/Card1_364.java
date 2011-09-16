package com.gempukku.lotro.cards.set1.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 4
 * Type: Companion • Wizard
 * Strength: 7
 * Vitality: 4
 * Resistance: 6
 * Signet: Gandalf
 * Game Text: Fellowship: Exert Gandalf to play a companion who has the Gandalf signet. The twilight cost of that
 * companion is -2.
 */
public class Card1_364 extends AbstractCompanion {
    public Card1_364() {
        super(4, 7, 4, Culture.GANDALF, Race.WIZARD, Signet.GANDALF, "Gandalf", true);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self)) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.FELLOWSHIP, "Exert Gandalf to play a companion who has the Gandalf signet. The twilight cost of that companion is -2.");
            action.addCost(new ExertCharacterEffect(playerId, self));
            action.addEffect(
                    new ChooseCardsFromHandEffect(playerId, "Choose companion to play", 1, 1, Filters.type(CardType.COMPANION), Filters.signet(Signet.GANDALF), Filters.playable(game)) {
                        @Override
                        protected void cardsSelected(List<PhysicalCard> selectedCards) {
                            PhysicalCard selectedCard = selectedCards.get(0);
                            game.getActionsEnvironment().addActionToStack(
                                    selectedCard.getBlueprint().getPlayCardAction(playerId, game, selectedCard, -2));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
