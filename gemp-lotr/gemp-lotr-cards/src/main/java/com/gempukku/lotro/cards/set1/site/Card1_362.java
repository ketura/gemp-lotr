package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.GameUtils;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 8
 * Type: Site
 * Site: 9
 * Game Text: When the fellowship moves to Summit of Amon Hen, each Shadow player may draw a card for each burden.
 */
public class Card1_362 extends AbstractSite {
    public Card1_362() {
        super("Summit of Amon Hen", 9, 8, Direction.LEFT);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_MOVE_TO
                && game.getGameState().getCurrentSite() == self) {
            List<OptionalTriggerAction> actions = new LinkedList<OptionalTriggerAction>();

            String fpPlayerId = game.getGameState().getCurrentPlayerId();
            final int burdens = game.getGameState().getBurdens();

            String[] opponents = GameUtils.getOpponents(game, fpPlayerId);
            for (String opponent : opponents) {
                final String opp = opponent;
                final OptionalTriggerAction action = new OptionalTriggerAction(self, null, "Shadow player may draw a card for each burden.");
                action.addEffect(
                        new PlayoutDecisionEffect(game.getUserFeedback(), opponent,
                                new MultipleChoiceAwaitingDecision(1, "Do you want to draw a card for each burder?", new String[]{"Yes", "No"}) {
                                    @Override
                                    protected void validDecisionMade(int index, String result) {
                                        if (result.equals("Yes")) {
                                            for (int i = 0; i < burdens; i++)
                                                action.addEffect(new DrawCardEffect(opp));
                                        }
                                    }
                                }));
                actions.add(action);
            }

            return actions;
        }
        return null;
    }
}
