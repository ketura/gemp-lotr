package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 3
 * Type: Minion � Uruk-Hai
 * Strength: 8
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. This minion is strength +1 for each other Uruk-hai you can spot.
 */
public class Card1_148 extends AbstractMinion {
    public Card1_148() {
        super(3, 8, 2, 5, Culture.ISENGARD, "Uruk Lieutenant", "1_148");
        addKeyword(Keyword.URUK_HAI);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public Modifier getAlwaysOnEffect(final PhysicalCard self) {
        return new AbstractModifier(self, "Strength +1 for each other Uruk-hai you can spot", Filters.sameCard(self)) {
            @Override
            public int getStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
                return result + Filters.countActive(gameState, modifiersQuerying, Filters.and(Filters.keyword(Keyword.URUK_HAI), Filters.not(Filters.sameCard(self))));
            }
        };
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame lotroGame, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        appendPlayMinionAction(actions, lotroGame, self);

        return actions;
    }
}
