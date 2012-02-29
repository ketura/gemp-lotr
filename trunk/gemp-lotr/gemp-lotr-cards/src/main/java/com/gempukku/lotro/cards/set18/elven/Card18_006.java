package com.gempukku.lotro.cards.set18.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.PutCardsFromDeckOnTopOfDrawDeckEffect;
import com.gempukku.lotro.cards.effects.ShuffleDeckEffect;
import com.gempukku.lotro.cards.effects.SpotEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 0
 * Type: Event • Maneuver
 * Game Text: To play, exert an [ELVEN] companion or spot Arwen. Examine the top 5 cards of your draw deck and place
 * any number of them aside. Shuffle the remaining cards into your draw deck and place those cards set aside on top
 * of your draw deck in any order.
 */
public class Card18_006 extends AbstractEvent {
    public Card18_006() {
        super(Side.FREE_PEOPLE, 0, Culture.ELVEN, "Back to the Light", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && (PlayConditions.canExert(self, game, Culture.ELVEN, CardType.COMPANION) || PlayConditions.canSpot(game, Filters.arwen));
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        List<Effect> possibleCosts = new LinkedList<Effect>();
        possibleCosts.add(
                new SpotEffect(1, Filters.arwen) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Spot Arwen";
                    }
                });
        possibleCosts.add(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.ELVEN, CardType.COMPANION) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Exert ELVEN companion";
                    }
                });
        action.appendCost(
                new ChoiceEffect(action, playerId, possibleCosts));

        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        final List<? extends PhysicalCard> deck = game.getGameState().getDeck(playerId);
                        final ArrayList<PhysicalCard> cards = new ArrayList<PhysicalCard>(deck.subList(0, Math.min(5, deck.size())));
                        action.appendEffect(
                                new ChooseArbitraryCardsEffect(playerId, "Choose cards to set aside", cards, 0, 5) {
                                    @Override
                                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                                        game.getGameState().sendMessage(playerId + " set aside " + selectedCards.size() + " cards");
                                        if (selectedCards.size() > 0)
                                            action.appendEffect(
                                                    new PutCardsFromDeckOnTopOfDrawDeckEffect(action, self, playerId, selectedCards));
                                    }
                                });
                        action.appendEffect(
                                new ShuffleDeckEffect(playerId));
                    }
                });
        return action;
    }
}
