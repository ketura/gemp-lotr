package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 9
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. Maneuver: Spot 6 companions to make this minion fierce until the regroup phase.
 */
public class Card1_156 extends AbstractMinion {
    public Card1_156() {
        super(3, 9, 2, 5, Culture.ISENGARD, "Uruk Warrior");
        addKeyword(Keyword.URUK_HAI);
        addKeyword(Keyword.DAMAGE);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();
        appendPlayMinionAction(actions, game, self);

        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SHADOW, self, 0)
                && Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION)) >= 6) {
            CostToEffectAction action = new CostToEffectAction(self, Keyword.MANEUVER, "Make this minion fierce until the regroup phase.");
            action.addEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new KeywordModifier(self, Filters.sameCard(self), Keyword.FIERCE), Phase.REGROUP));

            actions.add(action);
        }

        return null;
    }
}
