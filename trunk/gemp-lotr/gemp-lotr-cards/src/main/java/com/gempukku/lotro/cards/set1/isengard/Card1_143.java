package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.CancelStrengthBonusModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 5
 * Type: Minion • Uruk-Hai
 * Strength: 9
 * Vitality: 4
 * Site: 5
 * Game Text: Damage +1. A character skirmishing this minion does not gain strength bonuses from weapons.
 */
public class Card1_143 extends AbstractMinion {
    public Card1_143() {
        super(5, 9, 4, 5, Culture.ISENGARD, "Troop of Uruk-hai");
        addKeyword(Keyword.URUK_HAI);
        addKeyword(Keyword.DAMAGE);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        appendPlayMinionAction(actions, game, self);

        return actions;
    }

    @Override
    public Modifier getAlwaysOnEffect(final PhysicalCard self) {
        return new CancelStrengthBonusModifier(self, Filters.and(
                // Weapon bonus (Hand or Ranged)
                Filters.or(Filters.keyword(Keyword.HAND_WEAPON), Filters.keyword(Keyword.RANGED_WEAPON)),
                // That is attached to Companion or Ally
                Filters.attachedTo(Filters.or(Filters.type(CardType.COMPANION), Filters.type(CardType.ALLY))),
                // In a skirmish with this minion
                new com.gempukku.lotro.filters.Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        Skirmish skirmish = gameState.getSkirmish();
                        if (skirmish != null
                                && skirmish.getShadowCharacters().contains(self))
                            return true;
                        return false;
                    }
                }
        ));
    }
}
