package com.gempukku.lotro.cards.set10.raider;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

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
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game, Culture.RAIDER, Race.MAN);
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
