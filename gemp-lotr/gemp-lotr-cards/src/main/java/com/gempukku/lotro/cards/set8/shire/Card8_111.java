package com.gempukku.lotro.cards.set8.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: If the Shadow has initiative, add a threat to make an unbound Hobbit strength +1 for each [SHIRE] card
 * you spot.
 */
public class Card8_111 extends AbstractEvent {
    public Card8_111() {
        super(Side.FREE_PEOPLE, 1, Culture.SHIRE, "So Fair, So Desparate", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.hasInitiative(game, Side.SHADOW)
                && PlayConditions.canAddThreat(game, self, 1);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new AddThreatsEffect(playerId, self, 1));
        action.appendEffect(
                new ForEachYouSpotEffect(playerId, Culture.SHIRE) {
                    @Override
                    protected void spottedCards(int spotCount) {
                        action.appendEffect(
                                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, spotCount, Filters.unboundCompanion, Race.HOBBIT));
                    }
                });
        return action;
    }
}
