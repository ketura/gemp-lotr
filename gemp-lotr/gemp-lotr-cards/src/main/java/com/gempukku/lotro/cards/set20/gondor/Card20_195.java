package com.gempukku.lotro.cards.set20.gondor;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.modifiers.MinionSiteNumberModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * ❶ •Heightened Awareness [Gon]
 * Condition • Support Area
 * Each time you play a [Gon] event, the site number of each minion in play is +2 until the regroup phase
 * <p/>
 * http://lotrtcg.org/coreset/gondor/heightenedawareness(r3).jpg
 */
public class Card20_195 extends AbstractPermanent {
    public Card20_195() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GONDOR, "Heightened Awareness", null, true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Culture.GONDOR, CardType.EVENT)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new MinionSiteNumberModifier(self, CardType.MINION, null, 2), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
