package com.gempukku.lotro.cards.set10.raider;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 1
 * Type: Event • Archery
 * Game Text: Exert a [RAIDER] Man to discard a Free Peoples condition. If you spot a [ROHAN] Man, discard an additional
 * Free Peoples condition.
 */
public class Card10_046 extends AbstractEvent {
    public Card10_046() {
        super(Side.SHADOW, 1, Culture.RAIDER, "Quelled", Phase.ARCHERY);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Culture.RAIDER, Race.MAN);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.RAIDER, Race.MAN));
        int max = PlayConditions.canSpot(game, Culture.ROHAN, Race.MAN) ? 2 : 1;
        action.appendEffect(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, max, Side.FREE_PEOPLE, CardType.CONDITION));
        return action;
    }
}
