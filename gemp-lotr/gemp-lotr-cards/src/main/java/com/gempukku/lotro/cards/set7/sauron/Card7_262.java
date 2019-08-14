package com.gempukku.lotro.cards.set7.sauron;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromStackedEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return (PlayConditions.canPlayFromDiscard(self.getOwner(), game, Culture.SAURON, Race.ORC)
                && PlayConditions.canRemoveBurdens(game, self, 1))
                || PlayConditions.canPlayFromStacked(self.getOwner(), game, Filters.siteControlled(self.getOwner()), Keyword.BESIEGER);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        if (PlayConditions.canPlayFromStacked(playerId, game, Filters.siteControlled(playerId), Keyword.BESIEGER)
                && PlayConditions.canRemoveBurdens(game, self, 1)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.SAURON, Race.ORC)) {
            action.appendEffect(
                    new PlayoutDecisionEffect(playerId,
                            new MultipleChoiceAwaitingDecision(1, "Which would you like to do?", new String[]{"Play a besieger stacked on a site you control", "Remove a burden to play a SAURON Orc from Discard"}) {
                @Override
                protected void validDecisionMade(int index, String result) {
                    if (result.equals("Play a besieger stacked on a site you control")) {
                        action.appendEffect(
                        new ChooseAndPlayCardFromStackedEffect(playerId, Filters.siteControlled(playerId), Keyword.BESIEGER));
                    } else {
                        action.appendCost(
                                new RemoveBurdenEffect(playerId, self));
                        action.appendEffect(
                                new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.SAURON, Race.ORC));
                    }
                }
            }));
        } else if (PlayConditions.canPlayFromStacked(playerId, game, Filters.siteControlled(playerId), Keyword.BESIEGER)) {
            action.appendEffect(
                    new ChooseAndPlayCardFromStackedEffect(playerId, Filters.siteControlled(playerId), Keyword.BESIEGER));
        } else if (PlayConditions.canRemoveBurdens(game, self, 1)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.SAURON, Race.ORC)) {
            action.appendCost(
                    new RemoveBurdenEffect(playerId, self));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.SAURON, Race.ORC));
        }
        return action;
    }
}
