package com.gempukku.lotro.cards.set12.men;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: Each time a [MEN] minion is killed or discarded from play (except during the regroup phase), you may play
 * a [MEN] minion. Its twilight cost is -2, and it is fierce and strength +2 until the regroup phase.
 */
public class Card12_071 extends AbstractPermanent {
    public Card12_071() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.MEN, Zone.SUPPORT, "Last Days", null, true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.forEachDiscardedFromPlay(game, effectResult, CardType.MINION, Culture.MEN)
                && !PlayConditions.isPhase(game, Phase.REGROUP)
                && PlayConditions.canPlayFromHand(playerId, game, -2, Culture.MEN, CardType.MINION)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.setTriggerIdentifier(self.getCardId()+"-"+((DiscardCardsFromPlayResult) effectResult).getDiscardedCard().getCardId());
            action.appendEffect(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, -2, Culture.MEN, CardType.MINION) {
                        @Override
                        protected void afterCardPlayed(PhysicalCard cardPlayed) {
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new StrengthModifier(self, cardPlayed, 2), Phase.REGROUP));
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new KeywordModifier(self, cardPlayed, Keyword.FIERCE), Phase.REGROUP));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
