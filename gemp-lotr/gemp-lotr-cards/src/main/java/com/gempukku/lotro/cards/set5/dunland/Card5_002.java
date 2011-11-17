package com.gempukku.lotro.cards.set5.dunland;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 2
 * Type: Minion • Man
 * Strength: 10
 * Vitality: 1
 * Site: 3
 * Game Text: When you play this minion, the Free Peoples player may discard 2 cards from hand to discard him.
 */
public class Card5_002 extends AbstractMinion {
    public Card5_002() {
        super(2, 10, 1, 3, Race.MAN, Culture.DUNLAND, "Dunlending Renegade");
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(final LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Filters.sameCard(self))
                && game.getGameState().getHand(game.getGameState().getCurrentPlayerId()).size() >= 2) {
            final RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendCost(
                    new PlayoutDecisionEffect(game.getUserFeedback(), game.getGameState().getCurrentPlayerId(),
                            new MultipleChoiceAwaitingDecision(1, "Do you want to discard 2 cards from hand to discard this minion?", new String[]{"Yes", "No"}) {
                                @Override
                                protected void validDecisionMade(int index, String result) {
                                    if (result.equals("Yes")) {
                                        action.insertCost(
                                                new ChooseAndDiscardCardsFromHandEffect(action, game.getGameState().getCurrentPlayerId(), false, 2));
                                        action.appendEffect(
                                                new DiscardCardsFromPlayEffect(self, self));
                                    }
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
