package com.gempukku.lotro.cards.set3.gandalf;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.ShuffleCardsFromDiscardIntoDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseCardsFromDiscardEffect;
import com.gempukku.lotro.logic.timing.Effect;

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
        super(Side.FREE_PEOPLE, 1, Culture.GANDALF, "Fireworks", Phase.FELLOWSHIP);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Filters.gandalf);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        List<Effect> possibleEffects = new LinkedList<Effect>();
        possibleEffects.add(
                new ChooseCardsFromDiscardEffect(playerId, 0, 2, Culture.GANDALF) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Shuffle up to 2 GANDALF cards";
                    }

                    @Override
                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                        action.insertEffect(
                                new ShuffleCardsFromDiscardIntoDeckEffect(self, playerId, cards));
                    }
                });
        possibleEffects.add(
                new ChooseCardsFromDiscardEffect(playerId, 0, 2, Culture.SHIRE) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Shuffle up to 2 SHIRE cards";
                    }

                    @Override
                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                        action.insertEffect(
                                new ShuffleCardsFromDiscardIntoDeckEffect(self, playerId, cards));
                    }
                });
        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
    }
}
