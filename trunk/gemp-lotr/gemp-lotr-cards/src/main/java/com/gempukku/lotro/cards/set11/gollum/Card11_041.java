package com.gempukku.lotro.cards.set11.gollum;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: If a minion is skirmishing a Free Peoples character who has resistance 2 or less, spot Gollum to wound
 * that character twice.
 */
public class Card11_041 extends AbstractEvent {
    public Card11_041() {
        super(Side.SHADOW, 2, Culture.GOLLUM, "Frenzied Attack", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Filters.gollum)
                && PlayConditions.canSpot(game, CardType.MINION, Filters.inSkirmishAgainst(Side.FREE_PEOPLE, Filters.character, Filters.maxResistance(2)));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, 2, Filters.inSkirmishAgainst(CardType.MINION), Side.FREE_PEOPLE, Filters.character, Filters.maxResistance(2)));
        return action;
    }
}
