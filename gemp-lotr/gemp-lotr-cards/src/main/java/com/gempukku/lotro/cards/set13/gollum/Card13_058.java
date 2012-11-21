package com.gempukku.lotro.cards.set13.gollum;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 1
 * Type: Event • Shadow
 * Game Text: Spot Gollum to make the Free Peoples player choose to either add a burden or exert a companion.
 */
public class Card13_058 extends AbstractEvent {
    public Card13_058() {
        super(Side.SHADOW, 1, Culture.GOLLUM, "Wild Light of Madness", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Filters.gollum);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        List<Effect> possibleEffects = new LinkedList<Effect>();
        possibleEffects.add(
                new AddBurdenEffect(game.getGameState().getCurrentPlayerId(), self, 1));
        possibleEffects.add(
                new ChooseAndExertCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, CardType.COMPANION) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Exert a companion";
                    }
                });
        action.appendEffect(
                new ChoiceEffect(action, game.getGameState().getCurrentPlayerId(), possibleEffects));
        return action;
    }
}
