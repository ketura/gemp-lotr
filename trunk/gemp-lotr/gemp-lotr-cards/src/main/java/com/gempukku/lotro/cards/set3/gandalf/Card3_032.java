package com.gempukku.lotro.cards.set3.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ChooseCardsFromDiscardEffect;
import com.gempukku.lotro.cards.effects.PutCardFromDiscardOnBottomOfDeckEffect;
import com.gempukku.lotro.cards.effects.ShuffleDeckEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.ChooseableEffect;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Spell. Fellowship: Spot Gandalf to shuffle up to 2 [GANDALF] or up to 2 [SHIRE] cards from your discard
 * pile into your draw deck.
 */
public class Card3_032 extends AbstractEvent {
    public Card3_032() {
        super(Side.FREE_PEOPLE, Culture.GANDALF, "Fireworks", Phase.FELLOWSHIP);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name("Gandalf"));
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        List<ChooseableEffect> possibleEffects = new LinkedList<ChooseableEffect>();
        possibleEffects.add(
                new ChooseCardsFromDiscardEffect(playerId, 0, 2, Filters.culture(Culture.GANDALF)) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Shuffle up to 2 GANDALF cards";
                    }

                    @Override
                    protected void cardsSelected(Collection<PhysicalCard> cards) {
                        for (PhysicalCard card : cards) {
                            action.appendEffect(
                                    new PutCardFromDiscardOnBottomOfDeckEffect(card));
                        }
                        action.appendEffect(
                                new ShuffleDeckEffect(playerId));
                    }
                });
        possibleEffects.add(
                new ChooseCardsFromDiscardEffect(playerId, 0, 2, Filters.culture(Culture.SHIRE)) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Shuffle up to 2 SHIRE cards";
                    }

                    @Override
                    protected void cardsSelected(Collection<PhysicalCard> cards) {
                        for (PhysicalCard card : cards) {
                            action.appendEffect(
                                    new PutCardFromDiscardOnBottomOfDeckEffect(card));
                        }
                        action.appendEffect(
                                new ShuffleDeckEffect(playerId));
                    }
                });
        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
    }
}
