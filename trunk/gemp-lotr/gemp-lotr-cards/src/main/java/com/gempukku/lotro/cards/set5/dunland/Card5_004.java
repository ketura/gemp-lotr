package com.gempukku.lotro.cards.set5.dunland;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
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
 * Twilight Cost: 6
 * Type: Minion • Man
 * Strength: 18
 * Vitality: 2
 * Site: 3
 * Game Text: The twilight cost of this minion is -2 during the skirmish phase. When you play this minion, the Free
 * Peoples player may discard 4 cards from hand to discard it.
 */
public class Card5_004 extends AbstractMinion {
    public Card5_004() {
        super(6, 18, 2, 3, Race.MAN, Culture.DUNLAND, "Wild Men of the Hills");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        if (game.getGameState().getCurrentPhase() == Phase.SKIRMISH)
            twilightModifier -= 2;
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.MORIA), Filters.race(Race.ORC));
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        if (game.getGameState().getCurrentPhase() == Phase.SKIRMISH)
            twilightModifier -= 2;
        return super.getPlayCardAction(playerId, game, self, twilightModifier);
    }


    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(final LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (PlayConditions.played(game, effectResult, Filters.sameCard(self))
                && game.getGameState().getHand(game.getGameState().getCurrentPlayerId()).size() >= 4) {
            final RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendCost(
                    new PlayoutDecisionEffect(game.getUserFeedback(), game.getGameState().getCurrentPlayerId(),
                            new MultipleChoiceAwaitingDecision(1, "Do you want to discard 4 cards from hand to discard this minion?", new String[]{"Yes", "No"}) {
                                @Override
                                protected void validDecisionMade(int index, String result) {
                                    if (result.equals("Yes")) {
                                        action.insertCost(
                                                new ChooseAndDiscardCardsFromHandEffect(action, game.getGameState().getCurrentPlayerId(), false, 4));
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
