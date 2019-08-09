package com.gempukku.lotro.cards.set40.gandalf;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.RevealRandomCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;

import java.util.List;

/**
 * Title: Illuminate
 * Set: Second Edition
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event - Maneuver
 * Card Number: 1C78
 * Game Text: Spell.
 * Spot Gandalf to reveal a card at random from your opponent's hand. If it is a Free Peoples card, heal a companion.
 * If it is a Shadow card, exert a minion.
 */
public class Card40_078 extends AbstractEvent{
    public Card40_078() {
        super(Side.FREE_PEOPLE, 1, Culture.GANDALF, "Illuminate", Phase.MANEUVER);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Filters.gandalf);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new RevealRandomCardsFromHandEffect(playerId, GameUtils.getFirstShadowPlayer(game), self, 1) {
                    @Override
                    protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                        if (revealedCards.size()>0) {
                            PhysicalCard revealedCard = revealedCards.get(0);
                            if (Filters.and(Side.FREE_PEOPLE).accepts(game, revealedCard))
                                action.appendEffect(
                                        new ChooseAndHealCharactersEffect(action, playerId, CardType.COMPANION));
                            if (Filters.and(Side.SHADOW).accepts(game, revealedCard))
                                action.appendEffect(
                                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.MINION));
                        }
                    }
                });
        return action;
    }
}
