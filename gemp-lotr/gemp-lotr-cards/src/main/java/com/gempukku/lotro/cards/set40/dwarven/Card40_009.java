package com.gempukku.lotro.cards.set40.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardStackedCardsEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: Dwarf Soldier
 * Set: Second Edition
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Companion - Dwarf
 * Strength: 5
 * Vitality: 3
 * Resistance: 6
 * Card Number: 1C9
 * Game Text: Damage +1. Skirmish: Discard a card stacked on a [Dwarven] condition to make this companion strength +1.
 */
public class Card40_009 extends AbstractCompanion {
    public Card40_009() {
        super(2, 5, 3, 6, Culture.DWARVEN, Race.DWARF, null, "Dwarf Soldier");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canDiscardFromStacked(self, game, playerId, 1, Filters.and(Culture.DWARVEN, CardType.CONDITION), Filters.any)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardStackedCardsEffect(action, playerId, 1, 1, Filters.and(Culture.DWARVEN, CardType.CONDITION), Filters.any));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 1, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
