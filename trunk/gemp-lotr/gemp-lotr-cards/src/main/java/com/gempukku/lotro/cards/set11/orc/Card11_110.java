package com.gempukku.lotro.cards.set11.orc;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.RevealAndChooseCardsFromOpponentHandEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 1
 * Type: Event • Shadow
 * Game Text: Spot an [ORC] minion to reveal the Free Peoples player's hand. The Free Peoples player chooses to either
 * discard a revealed Free Peoples event or add a burden.
 */
public class Card11_110 extends AbstractEvent {
    public Card11_110() {
        super(Side.SHADOW, 1, Culture.ORC, "Bound to its Fate", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.ORC, CardType.MINION);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        final String fpPlayer = game.getGameState().getCurrentPlayerId();
        action.appendEffect(
                new RevealAndChooseCardsFromOpponentHandEffect(action, playerId, fpPlayer, self, "Opponent's hand", Filters.none, 0, 0) {
                    @Override
                    protected void cardsSelected(List<PhysicalCard> selectedCards) {
                        List<Effect> possibleEffects = new LinkedList<Effect>();
                        possibleEffects.add(
                                new ChooseAndDiscardCardsFromHandEffect(action, fpPlayer, false, 1, Side.FREE_PEOPLE, CardType.EVENT) {
                                    @Override
                                    public String getText(LotroGame game) {
                                        return "Discard a revealed Free Peoples event";
                                    }
                                });
                        possibleEffects.add(
                                new AddBurdenEffect(fpPlayer, self, 1));
                        action.appendEffect(
                                new ChoiceEffect(action, fpPlayer, possibleEffects));
                    }
                });
        return action;
    }
}
