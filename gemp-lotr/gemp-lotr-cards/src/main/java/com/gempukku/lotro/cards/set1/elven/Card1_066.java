package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.cards.modifiers.VitalityModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Condition
 * Vitality: +1
 * Game Text: Tale. Bearer must be an Elf. Skirmish: Discard this condition to make bearer strength +2.
 */
public class Card1_066 extends AbstractAttachable {
    public Card1_066() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 1, Culture.ELVEN, null, "The Tale of Gil-galad", true);
        addKeyword(Keyword.TALE);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.race(Race.ELF);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.SKIRMISH, "Discard this condition to make bearer strength +2");
            action.addCost(new DiscardCardFromPlayEffect(self, self));
            action.addEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, Filters.sameCard(self.getAttachedTo()), 2), Phase.SKIRMISH));

            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new VitalityModifier(self, Filters.isAttachedTo(self), 1);
    }
}
