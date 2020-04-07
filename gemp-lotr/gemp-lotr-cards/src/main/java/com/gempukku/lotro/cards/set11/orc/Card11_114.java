package com.gempukku.lotro.cards.set11.orc;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: To play, spot an [ORC] minion. Each time a companion exerts, you may add (1) (or (2) if you can spot
 * 6 companions).
 */
public class Card11_114 extends AbstractPermanent {
    public Card11_114() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.ORC, "Demoralized");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.ORC, CardType.MINION);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachExerted(game, effectResult, CardType.COMPANION)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            int count = PlayConditions.canSpot(game, 6, CardType.COMPANION) ? 2 : 1;
            action.appendEffect(
                    new AddTwilightEffect(self, count));
            return Collections.singletonList(action);
        }
        return null;
    }
}
