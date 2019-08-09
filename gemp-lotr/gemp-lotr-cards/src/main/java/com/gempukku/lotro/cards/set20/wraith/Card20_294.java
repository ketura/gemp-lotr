package com.gempukku.lotro.cards.set20.wraith;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.modifiers.ResistanceModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.MultiplyEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * •Lifting the Veil
 * Ringwraith	Condition • Support Area
 * Twilight. To play, spot a twilight Nazgul.
 * Each companion (except the Ring-bearer) is resistance -1 for each twilight card you can spot.
 * Discard this condition if a Nazgul loses a skirmish.
 */
public class Card20_294 extends AbstractPermanent {
    public Card20_294() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.WRAITH, "Lifting the Veil", null, true);
        addKeyword(Keyword.TWILIGHT);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Race.NAZGUL, Keyword.TWILIGHT);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new ResistanceModifier(self, Filters.and(CardType.COMPANION, Filters.not(Filters.ringBearer)),
                new MultiplyEvaluator(-1, new CountSpottableEvaluator(Keyword.TWILIGHT)));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.losesSkirmish(game, effectResult, Race.NAZGUL)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
