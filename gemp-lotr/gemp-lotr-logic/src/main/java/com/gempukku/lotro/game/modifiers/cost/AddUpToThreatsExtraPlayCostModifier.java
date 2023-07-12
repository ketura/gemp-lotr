package com.gempukku.lotro.game.modifiers.cost;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.game.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.game.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.game.effects.AddThreatsEffect;
import com.gempukku.lotro.game.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.game.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.game.modifiers.Condition;

public class AddUpToThreatsExtraPlayCostModifier extends AbstractExtraPlayCostModifier {
    private final int maxThreatCount;

    public AddUpToThreatsExtraPlayCostModifier(PhysicalCard source, int maxThreatCount, Condition condition, Filterable... affects) {
        super(source, "Add up to " + maxThreatCount + " threat(s)", Filters.and(affects), condition);
        this.maxThreatCount = maxThreatCount;
    }

    @Override
    public void appendExtraCosts(DefaultGame
                                         game, final CostToEffectAction action, final PhysicalCard card) {
        int maxThreats = Math.min(maxThreatCount, Filters.countActive(game, CardType.COMPANION) - game.getGameState().getThreats());
        action.appendCost(
                new PlayoutDecisionEffect(card.getOwner(),
                        new IntegerAwaitingDecision(1, "Choose how many threats to add", 0, maxThreats) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                int threats = getValidatedResult(result);
                                action.appendCost(
                                        new AddThreatsEffect(card.getOwner(), card, threats));
                            }
                        }));
    }

    @Override
    public boolean canPayExtraCostsToPlay(DefaultGame game, PhysicalCard card) {
        return true;
    }
}
