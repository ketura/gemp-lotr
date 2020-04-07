package com.gempukku.lotro.cards.set12.orc;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ForEachBurdenYouSpotEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 0
 * Type: Event • Skirmish
 * Game Text: Spot X burdens to make a character skirmishing an [ORC] Orc strength -X.
 */
public class Card12_081 extends AbstractEvent {
    public Card12_081() {
        super(Side.SHADOW, 0, Culture.ORC, "Abiding Evil", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ForEachBurdenYouSpotEffect(playerId) {
                    @Override
                    protected void burdensSpotted(int burdensSpotted) {
                        action.appendEffect(
                                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, -burdensSpotted, Filters.inSkirmishAgainst(Culture.ORC, Race.ORC)));
                    }
                });
        return action;
    }
}
