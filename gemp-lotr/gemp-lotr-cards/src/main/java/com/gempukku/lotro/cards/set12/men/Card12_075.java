package com.gempukku.lotro.cards.set12.men;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 4
 * Type: Event • Maneuver or Regroup
 * Game Text: Toil 2. (For each [MEN] character you exert when playing this, its twilight cost is -2) Add (1) for each
 * [MEN] card you can spot.
 */
public class Card12_075 extends AbstractEvent {
    public Card12_075() {
        super(Side.SHADOW, 4, Culture.MEN, "Poisonous Words", Phase.MANEUVER, Phase.REGROUP);
        addKeyword(Keyword.TOIL, 2);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        int count = Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Culture.MEN);
        action.appendEffect(
                new AddTwilightEffect(self, count));
        return action;
    }
}
