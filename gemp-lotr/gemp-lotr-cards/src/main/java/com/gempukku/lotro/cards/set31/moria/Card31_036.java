package com.gempukku.lotro.cards.set31.moria;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.RevealAndChooseCardsFromOpponentHandEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 10
 * Vitality: 3
 * Site: 4
 * Game Text: Fierce. Maneuver: Exert The Great Goblin twice to reveal the Free Peoples player's hand. Choose and
 * discard a revealed [DWARVEN] event.
 */
public class Card31_036 extends AbstractMinion {
    public Card31_036() {
        super(4, 10, 3, 4, Race.ORC, Culture.MORIA, "The Great Goblin", "Chieftain of the Misty Mountains", true);
		addKeyword(Keyword.FIERCE);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0)
                && PlayConditions.canSelfExert(self, 2, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendCost(
					new SelfExertEffect(action, self));
            action.appendEffect(
                new ChooseOpponentEffect(playerId) {
                    @Override
                    protected void opponentChosen(final String opponentId) {
                        action.appendEffect(
                                new RevealAndChooseCardsFromOpponentHandEffect(action, playerId, opponentId, self, "Choose a DWARVEN event", Filters.and(Culture.DWARVEN, CardType.EVENT), 0, 1) {
                            @Override
                            protected void cardsSelected(List<PhysicalCard> selectedCards) {
                                if (selectedCards.size() > 0) {
                                    action.appendEffect(new DiscardCardsFromHandEffect(self, opponentId, selectedCards, true));
                                }
                            }
						});
                    }
            });
            return Collections.singletonList(action);
        }
        return null;
    }
}