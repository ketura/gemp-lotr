package com.gempukku.lotro.cards.set10.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.PutPlayedEventOnTopOfDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Make a minion skirmishing an Elf strength -2. Spot a site in a support area to place this event on top
 * of your draw deck.
 */
public class Card10_010 extends AbstractEvent {
    public Card10_010() {
        super(Side.FREE_PEOPLE, 1, Culture.ELVEN, "Fleet-footed", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, -2, CardType.MINION, Filters.inSkirmishAgainst(Race.ELF)));
        if (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), CardType.SITE, Zone.SUPPORT))
            action.appendEffect(
                    new OptionalEffect(action, playerId,
                            new PutPlayedEventOnTopOfDeckEffect(action)));
        return action;
    }
}
