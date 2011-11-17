package com.gempukku.lotro.cards.set10.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: Each time you lose initiative, you may spot 2 [SHIRE] companions to remove a burden.
 */
public class Card10_108 extends AbstractPermanent {
    public Card10_108() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.SHIRE, Zone.SUPPORT, "A Light in His Mind", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.losesInitiative(effectResult, Side.FREE_PEOPLE)
                && PlayConditions.canSpot(game, 2, Culture.SHIRE, CardType.COMPANION)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new RemoveBurdenEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
