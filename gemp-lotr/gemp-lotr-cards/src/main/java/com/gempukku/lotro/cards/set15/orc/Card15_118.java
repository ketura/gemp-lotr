package com.gempukku.lotro.cards.set15.orc;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.modifiers.MinionSiteNumberModifier;
import com.gempukku.lotro.cards.modifiers.conditions.MinThreatCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.KillEffect;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: While you can spot a threat, the site number of each [ORC] Orc is -1. Regroup: Remove 3 threats
 * and discard 3 [ORC] Orcs from play to make the Free Peoples player spot a companion and place that companion in
 * the dead pile. Discard this condition.
 */
public class Card15_118 extends AbstractPermanent {
    public Card15_118() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.ORC, Zone.SUPPORT, "Unmistakable Omen", true);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new MinionSiteNumberModifier(self, Filters.and(Culture.ORC, Race.ORC), new MinThreatCondition(1), -1);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && PlayConditions.canRemoveThreat(game, self, 3)
                && PlayConditions.canDiscardFromPlay(self, game, 3, Culture.ORC, Race.ORC)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveThreatsEffect(self, 3));
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 3, 3, Culture.ORC, Race.ORC));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, game.getGameState().getCurrentPlayerId(), "Choose a companion to kill", CardType.COMPANION) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new KillEffect(card, KillEffect.Cause.CARD_EFFECT));
                        }
                    });
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
