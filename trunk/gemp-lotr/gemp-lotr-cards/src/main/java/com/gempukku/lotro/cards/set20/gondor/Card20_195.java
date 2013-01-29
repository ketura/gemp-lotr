package com.gempukku.lotro.cards.set20.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.MinionSiteNumberModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * Heightened Awareness
 * Gondor	Condition • Support Area
 * Each time you play a [Gondor] event, the site number of each minion is +1 until the regroup phase.
 */
public class Card20_195 extends AbstractPermanent {
    public Card20_195() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GONDOR, Zone.SUPPORT, "Heightened Awareness");
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Culture.GONDOR, CardType.EVENT)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new MinionSiteNumberModifier(self, Filters.in(Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), CardType.MINION)), null, 1), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
