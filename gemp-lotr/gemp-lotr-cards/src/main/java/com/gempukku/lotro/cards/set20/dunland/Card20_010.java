package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 4
 * • Dunlending Ruffian
 * Minion • Man
 * 9	2	3
 * When you play this minion, you may discard a [Dunland] card from hand. If you do, spot a companion (except
 * the Ring-bearer). At the start of the next assignment phase, assign this minion to that companion.
 * http://lotrtcg.org/coreset/dunland/dunlendingruffian(r1).png
 */
public class Card20_010 extends AbstractMinion {
    public Card20_010() {
        super(4, 9, 2, 3, Race.MAN, Culture.DUNLAND, "Dunlending Ruffian", null, true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canDiscardFromHand(game, playerId, 1, Culture.DUNLAND)) {
            final OptionalTriggerAction action= new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Culture.DUNLAND));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a companion", CardType.COMPANION, Filters.not(Filters.ringBearer)) {
                        @Override
                        protected void cardSelected(LotroGame game, final PhysicalCard companion) {
                            final int companionId = companion.getCardId();
                            final int minionId = self.getCardId();
                            action.appendEffect(
                                    new AddUntilEndOfPhaseActionProxyEffect(
                                            new AbstractActionProxy() {
                                                @Override
                                                public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
                                                    if (TriggerConditions.startOfPhase(game, effectResult, Phase.ASSIGNMENT)
                                                            && PlayConditions.isActive(game, companion) && companion.getCardId() == companionId
                                                            && PlayConditions.isActive(game, self) && self.getCardId() == minionId) {
                                                        RequiredTriggerAction action = new RequiredTriggerAction(self);
                                                        action.appendEffect(
                                                                new AssignmentEffect(playerId, companion, self));
                                                        return Collections.singletonList(action);
                                                    }
                                                    return null;
                                                }
                                            }, Phase.ASSIGNMENT));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
