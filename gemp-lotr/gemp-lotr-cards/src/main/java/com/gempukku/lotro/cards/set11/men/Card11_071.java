package com.gempukku.lotro.cards.set11.men;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 5
 * Type: Event • Skirmish
 * Game Text: Toil 2. (For each [MEN] character you exert when playing this, its twilight cost is -2.) Spot a [MEN]
 * minion to discard a possession from play.
 */
public class Card11_071 extends AbstractEvent {
    public Card11_071() {
        super(Side.SHADOW, 5, Culture.MEN, "Bold and Cunning", Phase.SKIRMISH);
        addKeyword(Keyword.TOIL, 2);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.MEN, CardType.MINION);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.POSSESSION));
        return action;
    }
}
