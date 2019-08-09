package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 5
 * Type: Event
 * Game Text: Maneuver: Exert a Nazgul to discard a Free Peoples possession or Free Peoples condition. If you can spot
 * no such card, discard an ally or companion (except the Ring-bearer) instead.
 */
public class Card1_205 extends AbstractEvent {
    public Card1_205() {
        super(Side.SHADOW, 5, Culture.WRAITH, "Beauty Is Fading", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Race.NAZGUL);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.NAZGUL));
        boolean firstEffect = Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Side.FREE_PEOPLE, Filters.or(CardType.POSSESSION, CardType.CONDITION));
        if (firstEffect) {
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Side.FREE_PEOPLE, Filters.or(CardType.POSSESSION, CardType.CONDITION)));
        } else {
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.or(CardType.ALLY, CardType.COMPANION), Filters.not(Filters.ringBearer)));
        }

        return action;
    }
}
