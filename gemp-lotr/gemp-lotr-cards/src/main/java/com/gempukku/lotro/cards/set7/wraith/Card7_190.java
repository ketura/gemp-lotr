package com.gempukku.lotro.cards.set7.wraith;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

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
 * Game Text: When you play this minion, you may spot a Nazgul to add 2 threats. The Free Peoples player may wound
 * the Ring-bearer to prevent this. Skirmish: Remove a threat to make this minion strength +3.
 */
public class Card7_190 extends AbstractMinion {
    public Card7_190() {
        super(2, 6, 2, 4, Race.ORC, Culture.WRAITH, "Morgul Destroyer");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, final LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, Race.NAZGUL)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new PreventableEffect(action,
                            new AddThreatsEffect(playerId, self, 2),
                            game.getGameState().getCurrentPlayerId(),
                            new PreventableEffect.PreventionCost() {
                                @Override
                                public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                    return new WoundCharactersEffect(self, game.getGameState().getRingBearer(playerId)) {
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
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canRemoveThreat(game, self, 1)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveThreatsEffect(self, 1));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, self, 3)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
