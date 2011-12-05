package com.gempukku.lotro.cards.set11.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 5
 * Type: Minion • Orc
 * Strength: 12
 * Vitality: 3
 * Site: 4
 * Game Text: Each time this minion wins a skirmish, add a burden unless the Free Peoples player discards a Free Peoples
 * event from hand.
 */
public class Card11_127 extends AbstractMinion {
    public Card11_127() {
        super(5, 12, 3, 4, Race.ORC, Culture.ORC, "Mocking Goblin");
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self)) {
            final RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new PreventableEffect(action,
                            new AddBurdenEffect(self, 1), game.getGameState().getCurrentPlayerId(),
                            new PreventableEffect.PreventionCost() {
                                @Override
                                public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                    return new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Side.FREE_PEOPLE, CardType.EVENT) {
                                        @Override
                                        public String getText(LotroGame game) {
                                            return "Discard a Free People event from hand";
                                        }
                                    };
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
