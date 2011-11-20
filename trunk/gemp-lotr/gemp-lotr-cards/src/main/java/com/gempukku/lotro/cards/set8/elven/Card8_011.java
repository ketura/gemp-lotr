package com.gempukku.lotro.cards.set8.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 0
 * Type: Event • Regroup
 * Game Text: To play, spot 2 Elves. Discard an Elf to draw 3 cards.
 */
public class Card8_011 extends AbstractEvent {
    public Card8_011() {
        super(Side.FREE_PEOPLE, 0, Culture.ELVEN, "Life of the Eldar", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, 2, Race.ELF)
                && PlayConditions.canDiscardFromPlay(self, game, Race.ELF);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Race.ELF));
        action.appendEffect(
                new DrawCardsEffect(playerId, 3));
        return action;
    }
}
