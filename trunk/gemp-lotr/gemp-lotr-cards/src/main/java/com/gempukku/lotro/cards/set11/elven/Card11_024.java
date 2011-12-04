package com.gempukku.lotro.cards.set11.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.PhysicalCard;

import java.util.List;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Exert an Elf to reveal the top card of your draw deck. If that card's twilight cost is 3 or less, make
 * a minion strength -3.
 */
public class Card11_024 extends AbstractEvent {
    public Card11_024() {
        super(Side.FREE_PEOPLE, 1, Culture.ELVEN, "Might of the Elf-lords", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Race.ELF);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.ELF));
        action.appendEffect(
                new RevealTopCardsOfDrawDeckEffect(self, playerId, 1) {
                    @Override
                    protected void cardsRevealed(List<PhysicalCard> cards) {
                        for (PhysicalCard card : cards) {
                            if (card.getBlueprint().getTwilightCost()<=3) {
                                action.appendEffect(
                                        new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, -3, CardType.MINION));
                            }
                        }
                    }
                });
        return action;
    }
}
