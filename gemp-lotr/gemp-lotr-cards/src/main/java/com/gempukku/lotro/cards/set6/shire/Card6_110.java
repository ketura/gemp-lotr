package com.gempukku.lotro.cards.set6.shire;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.RevealHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collection;
import java.util.HashSet;
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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.sam)
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
                                new RevealHandEffect(self, playerId, opponentId) {
                                    @Override
                                    protected void cardsRevealed(Collection<? extends PhysicalCard> cards) {
                                        Set<Culture> cultures = new HashSet<Culture>();
                                        for (PhysicalCard cardInHand : cards)
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
