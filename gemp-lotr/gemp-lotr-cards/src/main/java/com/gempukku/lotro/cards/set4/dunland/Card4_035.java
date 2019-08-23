package com.gempukku.lotro.cards.set4.dunland;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Maneuver: Spot 2 sites you control to make each of your [DUNLAND] Men fierce until the regroup phase.
 */
public class Card4_035 extends AbstractEvent {
    public Card4_035() {
        super(Side.SHADOW, 0, Culture.DUNLAND, "Wake of Destruction", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 2, Filters.siteControlled(self.getOwner()));
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new AddUntilStartOfPhaseModifierEffect(
                        new KeywordModifier(self, Filters.and(Filters.owner(playerId), Culture.DUNLAND, Race.MAN), Keyword.FIERCE), Phase.REGROUP));
        return action;
    }
}
