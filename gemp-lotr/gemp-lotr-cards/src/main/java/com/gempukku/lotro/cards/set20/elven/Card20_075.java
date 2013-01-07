package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Bow of the Galadhrim, Galadriel's Gift
 * Elven	Possession •  Ranged Weapon
 * 1
 * Bearer must be Legolas.
 * At the beginning of the archery phase, you may exert legolas to wound a minion.
 */
public class Card20_075 extends AbstractAttachableFPPossession {
    public Card20_075() {
        super(1, 1, 0, Culture.ELVEN, PossessionClass.RANGED_WEAPON, "Bow of the Galadhrim", "Galadriel's Gift", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.legolas;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.ARCHERY)
                && PlayConditions.canExert(self, game, Filters.legolas)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.legolas));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
