package com.gempukku.lotro.cards.set31.gollum;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Event • Shadow
 * Game Text: Discard an orc to play Gollum from your draw deck or discard pile.
 */
public class Card31_019 extends AbstractEvent {
    public Card31_019() {
        super(Side.SHADOW, 0, Culture.GOLLUM, "Better Than Nothing", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canDiscardFromPlay(self, game, Race.ORC);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Race.ORC));
        List<Effect> possibleEffects = new LinkedList<Effect>();
        possibleEffects.add(
                new ChooseAndPlayCardFromDeckEffect(playerId, Filters.gollum) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Play Gollum from your draw deck";
                    }
                });
        if (PlayConditions.canPlayFromDiscard(playerId, game, Filters.gollum)) {
            possibleEffects.add(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Filters.gollum) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Play Gollum from your discard pile";
                        }
                    });
        }
        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
    }
}
