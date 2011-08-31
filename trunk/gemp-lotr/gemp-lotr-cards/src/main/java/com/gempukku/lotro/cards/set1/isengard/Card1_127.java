package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
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
 * Twilight Cost: 7
 * Type: Minion • Uruk-Hai
 * Strength: 13
 * Vitality: 3
 * Site: 5
 * Game Text: Archer. Damage +1. Maneuver: Spot another Uruk-hai to make Lurtz fierce until the regroup phase.
 */
public class Card1_127 extends AbstractMinion {
    public Card1_127() {
        super(7, 13, 3, 5, Culture.ISENGARD, "Lurtz", "1_127", true);
        addKeyword(Keyword.URUK_HAI);
        addKeyword(Keyword.ARCHER);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        appendPlayMinionAction(actions, game, self);

        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.MANEUVER, self, 0)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.URUK_HAI), Filters.not(Filters.sameCard(self)))) {
            CostToEffectAction action = new CostToEffectAction(self, Keyword.MANEUVER, "Spot another Uruk-hai to make Lurtz fierce until the regroup phase.");
            action.addEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new KeywordModifier(self, Filters.sameCard(self), Keyword.FIERCE), Phase.REGROUP));

            actions.add(action);
        }
        return actions;
    }
}
