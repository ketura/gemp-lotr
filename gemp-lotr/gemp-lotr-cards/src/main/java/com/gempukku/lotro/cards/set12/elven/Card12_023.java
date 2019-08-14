package com.gempukku.lotro.cards.set12.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 3
 * Type: Event • Maneuver
 * Game Text: Toil 2. (For each [ELVEN] character you exert when playing this, its twilight cost is -2) Spot an Elf
 * companion to discard a condition from play.
 */
public class Card12_023 extends AbstractEvent {
    public Card12_023() {
        super(Side.FREE_PEOPLE, 3, Culture.ELVEN, "Seclusion", Phase.MANEUVER);
        addKeyword(Keyword.TOIL, 2);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Race.ELF, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.CONDITION));
        return action;
    }
}
