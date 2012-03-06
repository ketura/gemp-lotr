package com.gempukku.lotro.cards.set18.gollum;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Event • Shadow
 * Game Text: Spot a [GOLLUM] card to play a minion. Its twilight cost is -1 (or -3 if it is roaming).
 */
public class Card18_031 extends AbstractEvent {
    public Card18_031() {
        super(Side.SHADOW, 0, Culture.GOLLUM, "It Draws Him", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.GOLLUM)
                && (PlayConditions.canPlayFromHand(playerId, game, -1, CardType.MINION, Filters.not(Keyword.ROAMING))
                || PlayConditions.canPlayFromHand(playerId, game, -3, CardType.MINION, Keyword.ROAMING));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        List<Effect> possibleEffects = new LinkedList<Effect>();
        possibleEffects.add(
                new ChooseAndPlayCardFromHandEffect(playerId, game, -1, CardType.MINION, Filters.not(Keyword.ROAMING)) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Play not roaming minion";
                    }
                });
        possibleEffects.add(
                new ChooseAndPlayCardFromHandEffect(playerId, game, -3, CardType.MINION, Keyword.ROAMING) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Play roaming minion";
                    }
                });
        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
    }
}
