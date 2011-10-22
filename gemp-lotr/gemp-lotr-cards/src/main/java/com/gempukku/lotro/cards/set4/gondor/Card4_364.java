package com.gempukku.lotro.cards.set4.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
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
 * Set: The Two Towers
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 4
 * Type: Companion • Man
 * Strength: 8
 * Vitality: 4
 * Resistance: 6
 * Signet: Aragorn
 * Game Text: Ranger. Each time the fellowship moves, you may wound a minion for each unbound Hobbit you spot.
 */
public class Card4_364 extends AbstractCompanion {
    public Card4_364() {
        super(4, 8, 4, Culture.GONDOR, Race.MAN, Signet.ARAGORN, "Aragorn", true);
        addKeyword(Keyword.RANGER);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_FELLOWSHIP_MOVES
                && PlayConditions.canSpot(game, Race.HOBBIT, Filters.unboundCompanion)) {
            int count = Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Race.HOBBIT, Filters.unboundCompanion);
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            for (int i = 0; i < count; i++)
                action.appendEffect(
                        new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
