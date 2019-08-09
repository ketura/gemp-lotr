package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.SnapshotAndApplyStrengthModifierUntilStartOfPhaseEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Bag End
 * 1	0
 * Dwelling.
 * Fellowship: Discard a card from hand to make Frodo strength +1 until the regroup phase.
 */
public class Card20_416 extends AbstractSite {
    public Card20_416() {
        super("Bag End", Block.SECOND_ED, 1, 0, null);
        addKeyword(Keyword.DWELLING);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.FELLOWSHIP, self)
                && GameUtils.isFP(game, playerId)
                && PlayConditions.canDiscardFromHand(game, playerId, 1, Filters.any)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1));
            action.appendEffect(
                    new SnapshotAndApplyStrengthModifierUntilStartOfPhaseEffect(self,
                            new ConstantEvaluator(1), Phase.REGROUP, Filters.frodo));
            return Collections.singletonList(action);
        }
        return null;
    }
}
