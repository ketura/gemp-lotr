package com.gempukku.lotro.cards.set30.gundabad;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAssignCharacterToMinionEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.StackActionEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Main Deck
 * Side: Shadow
 * Culture: Gundabad
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 6
 * Vitality: 2
 * Site: 3
 * Game Text: Assignment: Assign this minion to skirmish Bilbo. The Free Peoples player may add a burden to
 * discard this minion.
 */
public class Card30_039 extends AbstractMinion {
    public Card30_039() {
        super(2, 6, 2, 3, Race.ORC, Culture.SMAUG, "Orkish Aggressor");
		addKeyword(Keyword.WARG_RIDER);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndAssignCharacterToMinionEffect(action, playerId, self, Filters.name("Bilbo")));
            String fpPlayer = game.getGameState().getCurrentPlayerId();
            action.appendEffect(
                    new PlayoutDecisionEffect(game.getGameState().getCurrentPlayerId(),
                            new MultipleChoiceAwaitingDecision(1, "Do you want to add a doubt to discard this minion?", new String[]{"Yes", "No"}) {
                @Override
                protected void validDecisionMade(int index, String result) {
                    if (result.equals("Yes")) {
                        action.insertCost(
                                new AddBurdenEffect(self.getOwner(), self, 1));
                        action.appendEffect(
                                new SelfDiscardEffect(self));
                    }
                }
			}));
            return Collections.singletonList(action);
        }
        return null;
    }
}