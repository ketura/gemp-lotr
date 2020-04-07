package com.gempukku.lotro.cards.set15.men;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExhaustCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Event • Archery
 * Game Text: Wound 3 [MEN] archers to exhaust a companion.
 */
public class Card15_084 extends AbstractEvent {
    public Card15_084() {
        super(Side.SHADOW, 3, Culture.MEN, "Last Gasp", Phase.ARCHERY);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canWound(self, game, 1, 3, Culture.MEN, Keyword.ARCHER);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndWoundCharactersEffect(action, playerId, 3, 3, Culture.MEN, Keyword.ARCHER));
        action.appendEffect(
                new ChooseAndExhaustCharactersEffect(action, playerId, 1, 1, CardType.COMPANION));
        return action;
    }
}
