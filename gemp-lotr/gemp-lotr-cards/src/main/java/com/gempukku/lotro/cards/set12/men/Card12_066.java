package com.gempukku.lotro.cards.set12.men;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 6
 * Type: Event • Skirmish
 * Game Text: Toil 2. (For each [MEN] character you exert when playing this, its twilight cost is -2) Make a [MEN]
 * minion strength +6.
 */
public class Card12_066 extends AbstractEvent {
    public Card12_066() {
        super(Side.SHADOW, 6, Culture.MEN, "Gathering Strength", Phase.SKIRMISH);
        addKeyword(Keyword.TOIL, 2);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 6, Culture.MEN, CardType.MINION));
        return action;
    }
}
