package com.gempukku.lotro.cards.set40.elven;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Title: Coordinated Strike
 * Set: Second Edition
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 3
 * Type: Condition - Support Area
 * Card Number: 1U39
 * Game Text: The twilight cost of this condition is -1 for each unwounded [ELVEN] archer companion you can spot.
 * Archery: Exert 2 [ELVEN] archer companions an discard this condition to make the fellowship archery total +2.
 */
public class Card40_039 extends AbstractPermanent{
    public Card40_039() {
        super(Side.FREE_PEOPLE, 3, CardType.CONDITION, Culture.ELVEN, Zone.SUPPORT, "Coordinated Strike");
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return -Filters.countSpottable(gameState, modifiersQuerying, Culture.ELVEN, Keyword.ARCHER, CardType.COMPANION, Filters.unwounded);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.ARCHERY, self)
                && PlayConditions.canExert(self, game, 1, 2, Culture.ELVEN, Keyword.ARCHER, CardType.COMPANION)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 2, 2, 1, Culture.ELVEN, Keyword.ARCHER, CardType.COMPANION));
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new ArcheryTotalModifier(self, Side.FREE_PEOPLE, 2)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
