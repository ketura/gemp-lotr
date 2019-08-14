package com.gempukku.lotro.cards.set2.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.PutCardFromDiscardOnBottomOfDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseCardsFromDiscardEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collection;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Fellowship: Exert a companion to place a Free Peoples card from your discard pile beneath your draw deck
 * (or 2 Free Peoples cards if that companion has the Frodo signet).
 */
public class Card2_098 extends AbstractEvent {
    public Card2_098() {
        super(Side.FREE_PEOPLE, 0, Culture.SHIRE, "Dear Friends", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION) {
                    @Override
                    protected void forEachCardExertedCallback(PhysicalCard character) {
                        Signet signet = character.getBlueprint().getSignet();
                        int count = (signet == Signet.FRODO) ? 2 : 1;
                        action.appendEffect(
                                new ChooseCardsFromDiscardEffect(playerId, count, count, Side.FREE_PEOPLE) {
                                    @Override
                                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                                        for (PhysicalCard card : cards) {
                                            action.insertEffect(
                                                    new PutCardFromDiscardOnBottomOfDeckEffect(card));
                                        }
                                    }
                                });
                    }
                });
        return action;
    }
}
