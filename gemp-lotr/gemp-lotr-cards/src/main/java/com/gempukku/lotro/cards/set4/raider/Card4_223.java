package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Maneuver: Exert a [RAIDER] Man to discard a Free Peoples condition (or 2 Free Peoples conditions if
 * you spot 3 burdens).
 */
public class Card4_223 extends AbstractEvent {
    public Card4_223() {
        super(Side.SHADOW, 1, Culture.RAIDER, "Discovered", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Culture.RAIDER, Race.MAN);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.RAIDER, Race.MAN));
        int maxDiscard = (game.getGameState().getBurdens() >= 3) ? 2 : 1;
        action.appendEffect(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, maxDiscard, Side.FREE_PEOPLE, CardType.CONDITION));
        return action;
    }
}
