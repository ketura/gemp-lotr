package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardStackedCardsEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * Fror, Gimli's Kinsman
 * Dwarven	Companion • Dwarf
 * 5	3	6
 * Damage +1.
 * Skirmish: Discard a card stacked on a [Dwarven] Condition to make Fror strength +1.
 */
public class Card20_052 extends AbstractCompanion {
    public Card20_052() {
        super(2, 5, 3, 6, Culture.DWARVEN, Race.DWARF, null, "Fror", "Gimli's Kinsman", true);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    protected List<ActivateCardAction> getExtraPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.isActive(game, Culture.DWARVEN, CardType.CONDITION, Filters.hasStacked(Filters.any))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardStackedCardsEffect(action, playerId, 1, 1, Filters.and(Culture.DWARVEN, CardType.CONDITION), Filters.any));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, self, 1)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
