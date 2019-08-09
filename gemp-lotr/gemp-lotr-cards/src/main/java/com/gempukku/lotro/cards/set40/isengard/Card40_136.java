package com.gempukku.lotro.cards.set40.isengard;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Title: Saruman's Ambition
 * Set: Second Edition
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Condition - Support Area
 * Card Number: 1C136
 * Game Text: The twilight cost of each [ISENGARD] event is -1. Skirmish: Discard this condition to make an Uruk-hai strength +2.
 */
public class Card40_136 extends AbstractPermanent {
    public Card40_136() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.ISENGARD, "Saruman's Ambition");
    }

    @Override
    public List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Race.URUK_HAI));

            return Collections.singletonList(action);
        }

        return null;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new TwilightCostModifier(self, Filters.and(Culture.ISENGARD, CardType.EVENT, Filters.owner(self.getOwner())), -1));
}
}
