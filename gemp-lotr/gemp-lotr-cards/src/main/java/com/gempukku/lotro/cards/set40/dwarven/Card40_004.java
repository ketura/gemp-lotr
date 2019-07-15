package com.gempukku.lotro.cards.set40.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;

/**
 * Title: Balin's Revenge
 * Set: Second Edition
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event - Skirmish
 * Card Number: 1C4
 * Game Text: Exert a Dwarf to wound up to two Goblins.
 */
public class Card40_004 extends AbstractEvent{
    public Card40_004(Side side, int twilightCost, Culture culture, String name, Phase playableInPhase, Phase... additionalPlayableInPhases) {
        super(Side.FREE_PEOPLE, 0, Culture.DWARVEN, "Balin's Revenge", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Race.DWARF);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, self.getOwner(), 1, 1, Race.DWARF));
        action.appendEffect(
                new ChooseAndWoundCharactersEffect(action, self.getOwner(), 0, 2, Race.GOBLIN));
        return action;
    }
}
