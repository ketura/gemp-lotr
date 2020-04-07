package com.gempukku.lotro.cards.set3.moria;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: To play, spot a [MORIA] Orc. Plays to your support area. Each time the fellowship moves during
 * the regroup phase, add (2).
 */
public class Card3_077 extends AbstractPermanent {
    public Card3_077() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.MORIA, "Depths of Moria");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Culture.MORIA, Race.ORC);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.moves(game, effectResult)
                && game.getGameState().getCurrentPhase() == Phase.REGROUP) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddTwilightEffect(self, 2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
