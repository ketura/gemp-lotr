package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentFromHandAction;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Possession
 * Game Text: Plays to your support area. Each Elf ally whose home is site 6 is strength +1. Maneuver: If an opponent
 * has at least 7 cards in hand, exert Galadriel to look at 2 of those cards at random. Discard one and replace the other.
 */
public class Card1_055 extends AbstractLotroCardBlueprint {
    public Card1_055() {
        super(Side.FREE_PEOPLE, CardType.POSSESSION, Culture.ELVEN, "The Mirror of Galadriel", "1_55", true);
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        if (PlayConditions.canPlayFromHandDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && !Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name("The Mirror of Galadriel")))
            actions.add(new PlayPermanentFromHandAction(self, Zone.FREE_SUPPORT));

        if (game.getGameState().getCurrentPhase() == Phase.MANEUVER
                && self.getZone() == Zone.FREE_SUPPORT
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name("Galadriel"), Filters.canExert())
                && opponentsHavingAtLeast7Cards(game, playerId).size() > 0) {

            CostToEffectAction action = new CostToEffectAction(self, "Exert Galadriel to look at 2 random cards, discard one");
            PhysicalCard galadriel = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name("Galadriel"));
            action.addCost(new ExertCharacterEffect(galadriel));
            action.addEffect(
                    new PlayoutDecisionEffect(game.getUserFeedback(), "playerId",
                            new MultipleChoiceAwaitingDecision(1, "Choose opponent with at least 7 cards in hand", opponentsHavingAtLeast7Cards(game, playerId).toArray(new String[0])) {
                                @Override
                                protected void validDecisionMade(int index, String result) {
                                    // TODO
                                }
                            }));

            actions.add(action);
        }
        return actions;
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(Filters.type(CardType.ALLY), Filters.siteNumber(6)), 1);
    }

    private List<String> opponentsHavingAtLeast7Cards(LotroGame game, String currentPlayer) {
        List<String> shadowPlayers = new LinkedList<String>(game.getGameState().getPlayerOrder().getAllPlayers());
        shadowPlayers.remove(currentPlayer);

        List<String> result = new LinkedList<String>();

        for (String shadowPlayer : shadowPlayers)
            if (game.getGameState().getHand(shadowPlayer).size() > 6)
                result.add(shadowPlayer);

        return result;
    }
}
