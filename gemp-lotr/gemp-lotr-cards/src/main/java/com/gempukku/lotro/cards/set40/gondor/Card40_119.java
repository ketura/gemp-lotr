package com.gempukku.lotro.cards.set40.gondor;

import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.modifiers.VitalityModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Title: *The Saga of Elendil
 * Set: Second Edition
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Condition - Companion
 * Vitality: +1
 * Card Number: 1R119
 * Game Text: Tale. Bearer must be a [GONDOR] Man. Skirmish: Discard this condition to make bearer strength +2.
 */
public class Card40_119 extends AbstractAttachable {
    public Card40_119() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 1, Culture.GONDOR, null, "The Saga of Elendil", null, true);
        addKeyword(Keyword.TALE);
    }

    @Override
    public Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GONDOR, CardType.COMPANION);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new SelfDiscardEffect(self));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, Filters.sameCard(self.getAttachedTo()), 2)));

            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new VitalityModifier(self, Filters.hasAttached(self), 1);
    }
}
