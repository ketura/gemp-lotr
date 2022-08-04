package com.gempukku.lotro.cards.set7.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.KillEffect;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.AddUpToThreatsExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.SpotExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.*;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: To play, spot 2 Nazgul and add up to 3 threats. Each time a companion is about to be killed, you may
 * remove a threat to discard up to 2 possessions. Discard this condition and remove 3 threats during the regroup phase.
 */
public class Card7_181 extends AbstractPermanent {
    public Card7_181() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.WRAITH, "Held Ground", null, true);
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlay(LotroGame game, PhysicalCard self) {
        return Arrays.asList(
                new SpotExtraPlayCostModifier(self, self, null, 2, Race.NAZGUL),
                new AddUpToThreatsExtraPlayCostModifier(self, 3, null, self));
    }

    @Override
    public List<OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingKilled(effect, game, CardType.COMPANION)) {
            KillEffect killEffect = (KillEffect) effect;
            Collection<PhysicalCard> companionsKilled = Filters.filter(killEffect.getCharactersToBeKilled(), game, CardType.COMPANION);
            List<OptionalTriggerAction> actions = new LinkedList<>();
            for (PhysicalCard physicalCard : companionsKilled) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                action.appendCost(
                        new RemoveThreatsEffect(self, 1));
                action.appendEffect(
                        new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 0, 2, CardType.POSSESSION));
                actions.add(action);
            }
            return actions;
        }
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.REGROUP)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new RemoveThreatsEffect(self, 3));
            return Collections.singletonList(action);
        }
        return null;
    }
}
