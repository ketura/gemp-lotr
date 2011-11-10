package com.gempukku.lotro.cards.set6.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.RevealAndChooseCardsFromOpponentHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Regroup: Spot Sam and Smeagol (or Gollum) to choose an opponent who must reveal his or her hand. Wound
 * a minion X times, where X is the number of different cultures revealed.
 */
public class Card6_110 extends AbstractEvent {
    public Card6_110() {
        super(Side.FREE_PEOPLE, 1, Culture.SHIRE, "It Burns Us", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, Filters.sam)
                && PlayConditions.canSpot(game, Filters.gollumOrSmeagol);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseOpponentEffect(playerId) {
                    @Override
                    protected void opponentChosen(final String opponentId) {
                        action.insertEffect(
                                new RevealAndChooseCardsFromOpponentHandEffect(action, playerId, opponentId, self, "Opponent's hand", Filters.none, 0, 0) {
                                    @Override
                                    protected void cardsSelected(List<PhysicalCard> selectedCards) {
                                        Set<Culture> cultures = new HashSet<Culture>();
                                        for (PhysicalCard cardInHand : game.getGameState().getHand(opponentId))
                                            cultures.add(cardInHand.getBlueprint().getCulture());

                                        action.appendEffect(
                                                new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, cultures.size(), CardType.MINION));
                                    }
                                });
                    }
                });
        return action;
    }
}
