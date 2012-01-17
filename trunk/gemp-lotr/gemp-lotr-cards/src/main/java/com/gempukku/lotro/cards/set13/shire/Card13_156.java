package com.gempukku.lotro.cards.set13.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.TransferToSupportEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 5
 * Game Text: Ring-bound. Sam is resistance +1 for each Hobbit you can spot. Regroup: Exert Sam and transfer a follower
 * he is bearing to your support area to discard a minion from play.
 */
public class Card13_156 extends AbstractCompanion {
    public Card13_156() {
        super(2, 3, 4, 5, Culture.SHIRE, Race.HOBBIT, null, "Sam", true);
        addKeyword(Keyword.RING_BOUND);
        addKeyword(Keyword.CAN_START_WITH_RING);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new ResistanceModifier(self, self, new CountSpottableEvaluator(Race.HOBBIT));
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canSelfExert(self, game)
                && PlayConditions.canSpot(game, self, Filters.hasAttached(CardType.FOLLOWER))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(self));
            action.appendCost(
                    new ChooseActiveCardEffect(self, playerId, "Choose a follower", CardType.FOLLOWER, Filters.attachedTo(self)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendCost(
                                    new TransferToSupportEffect(card));
                        }
                    });
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
