package com.gempukku.lotro.cards.set7.gondor;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.cards.modifiers.MinionSiteNumberModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Ally • Home 3T • Man
 * Strength: 6
 * Vitality: 1
 * Site: 3T
 * Game Text: Maneuver: If you have initiative, discard 2 cards from hand to make the site number of a minion +2 until
 * the regroup phase.
 */
public class Card7_097 extends AbstractAlly {
    public Card7_097() {
        super(1, Block.TWO_TOWERS, 3, 6, 1, Race.MAN, Culture.GONDOR, "Gondorian Merchant", true);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.MANEUVER, self)
                && PlayConditions.hasInitiative(game, Side.FREE_PEOPLE)
                && PlayConditions.canDiscardFromHand(game, playerId, 2, Filters.any)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 2));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a minion", CardType.MINION) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            game.getModifiersEnvironment().addUntilStartOfPhaseModifier(
                                    new MinionSiteNumberModifier(self, card, null, 2), Phase.REGROUP);
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
