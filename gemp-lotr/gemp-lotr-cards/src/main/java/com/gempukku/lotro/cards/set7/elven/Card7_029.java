package com.gempukku.lotro.cards.set7.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.PutPlayedEventOnTopOfDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: Make an Elf strength +2. You may place this event on top of your draw deck.
 */
public class Card7_029 extends AbstractEvent {
    public Card7_029() {
        super(Side.FREE_PEOPLE, 2, Culture.ELVEN, "Still Needed", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Race.ELF));
        action.appendEffect(
                new OptionalEffect(action, playerId,
                        new PutPlayedEventOnTopOfDeckEffect(action)));
        return action;
    }
}
