package com.gempukku.lotro.cards.set7.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 6
 * Vitality: 2
 * Site: 4
 * Game Text: When you play this minion, you may spot a Nazgul to add a burden. The Free Peoples player may wound
 * the Ring-bearer to prevent this. Skirmish: Remove a burden to make this minion strength +3.
 */
public class Card7_188 extends AbstractMinion {
    public Card7_188() {
        super(2, 6, 2, 4, Race.ORC, Culture.WRAITH, "Morgul Brute");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (PlayConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, Race.NAZGUL)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new PreventableEffect(action,
                            new AddBurdenEffect(self, 1),
                            game.getGameState().getCurrentPlayerId(),
                            new PreventableEffect.PreventionCost() {
                                @Override
                                public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                    return new WoundCharactersEffect(self, Keyword.RING_BEARER) {
                                        @Override
                                        public String getText(LotroGame game) {
                                            return "Wound Ring-bearer";
                                        }
                                    };
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canRemoveBurdens(game, self, 1)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveBurdenEffect(self));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, self, 3), Phase.SKIRMISH));
            return Collections.singletonList(action);
        }
        return null;
    }
}
