package com.gempukku.lotro.cards.set17.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Event • Maneuver
 * Game Text: Spot 2 Elves (or an Elf and an [ELVEN] follower) to discard a condition (or discard two conditions if you
 * can spot 4 or more Shadow conditions).
 */
public class Card17_009 extends AbstractEvent {
    public Card17_009() {
        super(Side.FREE_PEOPLE, 2, Culture.ELVEN, "Lothlorien Guides", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && (
                PlayConditions.canSpot(game, 2, Race.ELF)
                        || (PlayConditions.canSpot(game, Race.ELF) && PlayConditions.canSpot(game, CardType.FOLLOWER, Culture.ELVEN)));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        int count = PlayConditions.canSpot(game, 4, Side.SHADOW, CardType.CONDITION) ? 2 : 1;
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, count, count, CardType.CONDITION));
        return action;
    }
}
