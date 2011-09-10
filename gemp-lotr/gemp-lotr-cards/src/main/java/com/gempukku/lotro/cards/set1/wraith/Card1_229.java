package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.CheckLimitEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 6
 * Type: Minion • Nazgul
 * Strength: 12
 * Vitality: 3
 * Site: 3
 * Game Text: Fierce. Skirmish: Remove (1) to make Ulaire Attea strength +1 (limit +5).
 */
public class Card1_229 extends AbstractMinion {
    public Card1_229() {
        super(6, 12, 3, 3, Keyword.NAZGUL, Culture.WRAITH, "Ulaire Attea", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self, 1)) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.SKIRMISH, "Remove (1) to make Ulaire Attea strength +1 (limit +5).");
            action.addCost(new RemoveTwilightEffect(1));
            action.addEffect(
                    new CheckLimitEffect(action, self, 5, Phase.SKIRMISH, false,
                            new AddUntilEndOfPhaseModifierEffect(
                                    new StrengthModifier(self, Filters.sameCard(self), 1), Phase.SKIRMISH)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
