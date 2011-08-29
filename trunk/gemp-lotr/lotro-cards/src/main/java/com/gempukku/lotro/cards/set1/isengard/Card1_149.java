package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.CardType;
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
 * Twilight Cost: 2
 * Type: Minion � Uruk-Hai
 * Strength: 6
 * Vitality: 1
 * Site: 5
 * Game Text: Damage +1. While you can spot a weather condition, this minion is strength +3.
 */
public class Card1_149 extends AbstractMinion {
    public Card1_149() {
        super(2, 6, 1, 5, Culture.ISENGARD, "Uruk Messenger", "1_149");
        addKeyword(Keyword.URUK_HAI);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new AbstractModifier(self, "While you can spot a weather condition, this minion is strength +3", Filters.sameCard(self)) {
            @Override
            public int getStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
                if (Filters.canSpot(gameState, modifiersQuerying, Filters.keyword(Keyword.WEATHER), Filters.type(CardType.CONDITION)))
                    return 3 + result;
                return result;
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
