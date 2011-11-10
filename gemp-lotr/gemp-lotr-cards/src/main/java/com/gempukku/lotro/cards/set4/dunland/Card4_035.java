package com.gempukku.lotro.cards.set4.dunland;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Maneuver: Spot 2 sites you control to make each of your [DUNLAND] Men fierce until the regroup phase.
 */
public class Card4_035 extends AbstractOldEvent {
    public Card4_035() {
        super(Side.SHADOW, Culture.DUNLAND, "Wake of Destruction", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Filters.siteControlled(playerId)) >= 2;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new AddUntilStartOfPhaseModifierEffect(
                        new KeywordModifier(self, Filters.and(Filters.owner(playerId), Filters.culture(Culture.DUNLAND), Filters.race(Race.MAN)), Keyword.FIERCE), Phase.REGROUP));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
