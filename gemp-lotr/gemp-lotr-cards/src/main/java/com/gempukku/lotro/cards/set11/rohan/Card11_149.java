package com.gempukku.lotro.cards.set11.rohan;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 4
 * Type: Condition
 * Game Text: Toil 2. (For each [ROHAN] character you exert when playing this, its twilight cost is -2.) Bearer must be
 * a [ROHAN] Man. Assignment: Discard this condition from play to make bearer defender +1.
 */
public class Card11_149 extends AbstractAttachable {
    public Card11_149() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 4, Culture.ROHAN, null, "Protecting the Hall");
        addKeyword(Keyword.TOIL, 2);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.ROHAN, Race.MAN);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new KeywordModifier(self, self.getAttachedTo(), Keyword.DEFENDER, 1), Phase.ASSIGNMENT));
            return Collections.singletonList(action);
        }
        return null;
    }
}
