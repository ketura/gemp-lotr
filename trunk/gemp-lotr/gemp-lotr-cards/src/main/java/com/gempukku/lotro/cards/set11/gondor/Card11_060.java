package com.gempukku.lotro.cards.set11.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.CardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;

import java.util.Set;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: Exert any number of [GONDOR] companions who have total resistance 12 or more to make a minion skirmishing
 * a [GONDOR] companion strength -3 for each companion exerted this way.
 */
public class Card11_060 extends AbstractEvent {
    public Card11_060() {
        super(Side.FREE_PEOPLE, 2, Culture.GONDOR, "The Highest Quality", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && canExertMinResistanceGondorCompanions(game, self, 12);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new PlayoutDecisionEffect(playerId,
                        new CardsSelectionDecision(1, "Choose GONDOR companions to exert with a total resistance of 12 or more", Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Culture.GONDOR, CardType.COMPANION, Filters.canExert(self)), 1, Integer.MAX_VALUE) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                Set<PhysicalCard> characters = getSelectedCardsByResponse(result);
                                int resistanceTotal = 0;
                                for (PhysicalCard character : characters)
                                    resistanceTotal += game.getModifiersQuerying().getResistance(game.getGameState(), character);
                                if (resistanceTotal < 12)
                                    throw new DecisionResultInvalidException("These characters have only " + resistanceTotal + " resistance total");

                                action.insertCost(
                                        new ExertCharactersEffect(action, self, characters.toArray(new PhysicalCard[characters.size()])));
                                action.appendEffect(
                                        new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, -3 * characters.size(), CardType.MINION, Filters.inSkirmishAgainst(Culture.GONDOR, CardType.COMPANION)));
                            }
                        }));
        return action;
    }

    private boolean canExertMinResistanceGondorCompanions(LotroGame game, PhysicalCard self, int resistance) {
        int resistanceTotal = 0;
        for (PhysicalCard physicalCard : Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION, Culture.GONDOR, Filters.canExert(self))) {
            resistanceTotal += game.getModifiersQuerying().getResistance(game.getGameState(), physicalCard);
        }
        return resistanceTotal >= resistance;
    }
}
