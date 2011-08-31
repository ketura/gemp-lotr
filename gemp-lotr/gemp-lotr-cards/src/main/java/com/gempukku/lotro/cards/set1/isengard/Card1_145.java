package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.decisions.ForEachYouSpotDecision;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 4
 * Type: Minion � Uruk-Hai
 * Strength: 9
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. Skirmish: Remove (2) to make this minion strength +1 for each other Uruk-hai you spot.
 */
public class Card1_145 extends AbstractMinion {
    public Card1_145() {
        super(4, 9, 2, 5, Culture.ISENGARD, "Uruk Brood", "1_145");
        addKeyword(Keyword.URUK_HAI);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, final LotroGame lotroGame, final PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        appendPlayMinionAction(actions, lotroGame, self);

        if (PlayConditions.canUseShadowCardDuringPhase(lotroGame.getGameState(), Phase.SKIRMISH, self, 2)) {
            final CostToEffectAction action = new CostToEffectAction(self, "Remove (2) to make this minion strength +1 for each other Uruk-hai you spot");

            action.addCost(new RemoveTwilightEffect(2));
            action.addEffect(
                    new PlayoutDecisionEffect(lotroGame.getUserFeedback(), playerId,
                            new ForEachYouSpotDecision(1, "Choose number of minions you wish to spot", lotroGame, Filters.and(Filters.keyword(Keyword.URUK_HAI), Filters.not(Filters.sameCard(self))), Integer.MAX_VALUE) {
                                @Override
                                public void decisionMade(String result) throws DecisionResultInvalidException {
                                    int spotCount = getValidatedResult(result);
                                    action.addEffect(
                                            new AddUntilEndOfPhaseModifierEffect(
                                                    new StrengthModifier(self, Filters.sameCard(self), spotCount)
                                                    , Phase.SKIRMISH));
                                }
                            }
                    ));

            actions.add(action);
        }

        return actions;
    }
}
