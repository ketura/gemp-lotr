package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardStackedCardsEffect;
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
 * 2
 * Dwarven Axe
 * Dwarven	Possession • Hand Weapon
 * 2	1
 * Bearer must be a Dwarf
 * Each time bearer wins a skirmish, you may discard a card stacked on a [Dwarven] condition to wound a minion.
 */
public class Card20_044 extends AbstractAttachableFPPossession {
    public Card20_044() {
        super(2, 2, 1, Culture.DWARVEN, PossessionClass.HAND_WEAPON, "Dwarven Axe");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.DWARF;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.hasAttached(self))
                && PlayConditions.canSpot(game, Culture.DWARVEN, CardType.CONDITION, Filters.hasStacked(Filters.any))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndDiscardStackedCardsEffect(action, playerId, 1, 1, Filters.and(Culture.DWARVEN, CardType.CONDITION), Filters.any));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
