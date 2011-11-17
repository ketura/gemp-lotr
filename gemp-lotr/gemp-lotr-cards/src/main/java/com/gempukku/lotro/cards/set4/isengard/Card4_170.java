package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.AssignmentResult;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Each time the Free Peoples player assigns an ally to a skirmish, you may play
 * an [ISENGARD] minion from your discard pile. That minion's twilight cost is -2.
 */
public class Card4_170 extends AbstractPermanent {
    public Card4_170() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.ISENGARD, Zone.SUPPORT, "Ranks Without Number", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.ASSIGNMENT) {
            AssignmentResult assignmentResult = (AssignmentResult) effectResult;
            if (assignmentResult.getPlayerId().equals(game.getGameState().getCurrentPlayerId())) {
                int count = Filters.filter(assignmentResult.getAssignments().keySet(), game.getGameState(), game.getModifiersQuerying(), CardType.ALLY).size();
                List<OptionalTriggerAction> optionalTriggers = new LinkedList<OptionalTriggerAction>();

                for (int i = 0; i < count; i++) {
                    OptionalTriggerAction action = new OptionalTriggerAction(self);
                    action.appendEffect(
                            new ChooseAndPlayCardFromDiscardEffect(playerId, game.getGameState().getDiscard(playerId), -2, Filters.and(CardType.MINION, Filters.culture(Culture.ISENGARD))));
                    optionalTriggers.add(action);
                }

                return optionalTriggers;
            }
        }
        return null;
    }
}
