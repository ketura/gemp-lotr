package com.gempukku.lotro.cards.set11.wraith;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.ExhaustCharacterEffect;
import com.gempukku.lotro.logic.effects.PayTwilightCostEffect;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: To play, spot a Nazgul. Each time the Free Peoples player plays a companion, that companion is exhausted
 * unless the Free Peoples player pays its twilight cost again.
 */
public class Card11_218 extends AbstractPermanent {
    public Card11_218() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.WRAITH, "Surrounded by Wraiths", null, true);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Race.NAZGUL);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, CardType.COMPANION)) {
            PlayCardResult playedResult = (PlayCardResult) effectResult;
            final PhysicalCard playedCompanion = playedResult.getPlayedCard();

            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new PreventableEffect(action,
                            new ExhaustCharacterEffect(self, action, playedCompanion), game.getGameState().getCurrentPlayerId(),
                            new PreventableEffect.PreventionCost() {
                                @Override
                                public Effect createPreventionCostForPlayer(CostToEffectAction subAction, String playerId) {
                                    return new PayTwilightCostEffect(playedCompanion) {
                                        @Override
                                        public String getText(LotroGame game) {
                                            return "Pay twilight cost again";
                                        }
                                    };
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
