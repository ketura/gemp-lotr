package com.gempukku.lotro.cards.set7.sauron;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromStackedEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Event • Shadow
 * Game Text: Play a bieseger stacked on a site you control or remove a burden to play a [SAURON] Orc from your discard
 * pile.
 */
public class Card7_262 extends AbstractEvent {
    public Card7_262() {
        super(Side.SHADOW, 0, Culture.SAURON, "Above the Battlement", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.SAURON, Race.ORC)
                && (PlayConditions.canRemoveBurdens(game, self, 1)
                || PlayConditions.canPlayFromStacked(playerId, game, Filters.siteControlled(playerId), Keyword.BESIEGER));
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        List<Effect> possibleCosts = new LinkedList<Effect>();
        possibleCosts.add(
                new ChooseAndPlayCardFromStackedEffect(playerId, Filters.siteControlled(playerId), Keyword.BESIEGER) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Play a besieger stacked on a site you control";
                    }
                });
        possibleCosts.add(
                new RemoveBurdenEffect(playerId, self));
        action.appendCost(
                new ChoiceEffect(action, playerId, possibleCosts));

        action.appendEffect(
                new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.SAURON, Race.ORC));
        return action;
    }
}
