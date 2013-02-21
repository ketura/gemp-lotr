package com.gempukku.lotro.cards.set20.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.modifiers.FpSkirmishResistanceStrengthOverrideModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * 4
 * •Ulaire Nertea, Twilight Fiend
 * Ringwraith	Minion • Nazgul
 * 9	3	3
 * Twilight. Exert Nertea to make characters skirmishing him use resistance instead of strength to resolve skirmishes.
 */
public class Card20_309 extends AbstractMinion {
    public Card20_309() {
        super(4, 9, 3, 3, Race.NAZGUL, Culture.WRAITH, Names.nertea, "Twilight Fiend", true);
        addKeyword(Keyword.TWILIGHT);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new FpSkirmishResistanceStrengthOverrideModifier(self, Filters.and(Filters.character, Filters.inSkirmishAgainst(self)), null)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
