package com.gempukku.lotro.rules.lotronly;

import com.gempukku.lotro.actions.AbstractActionProxy;
import com.gempukku.lotro.actions.OptionalTriggerAction;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.effects.AddTwilightEffect;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.effects.EffectResult;
import com.gempukku.lotro.rules.GameUtils;
import com.gempukku.lotro.effects.results.AssignAgainstResult;

import java.util.Collections;
import java.util.List;

public class AmbushRule {
    private final DefaultActionsEnvironment _actionsEnvironment;

    public AmbushRule(DefaultActionsEnvironment actionsEnvironment) {
        _actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        _actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends OptionalTriggerAction> getOptionalAfterTriggerActions(String playerId, DefaultGame game, EffectResult effectResult) {
                        if (effectResult.getType() == EffectResult.Type.ASSIGNED_AGAINST) {
                            AssignAgainstResult assignmentResult = (AssignAgainstResult) effectResult;
                            if (assignmentResult.getPlayerId().equals(game.getGameState().getCurrentPlayerId())) {
                                LotroPhysicalCard assignedCard = assignmentResult.getAssignedCard();
                                if (Filters.and(CardType.MINION, Keyword.AMBUSH, Filters.owner(playerId)).accepts(game, assignedCard)) {
                                    final int count = game.getModifiersQuerying().getKeywordCount(game, assignedCard, Keyword.AMBUSH);
                                    OptionalTriggerAction action = new OptionalTriggerAction("ambush" + assignedCard.getCardId(), assignedCard);
                                    action.setMessage(playerId + " uses Ambush (" + count + ") from " + GameUtils.getCardLink(assignedCard));
                                    action.setText("Ambush - add " + count);
                                    action.appendEffect(
                                            new AddTwilightEffect(assignedCard, count));
                                    return Collections.singletonList(action);
                                }
                            }
                        }
                        return null;
                    }
                });
    }
}
