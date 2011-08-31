package com.gempukku.lotro.cards.set1.isengard;

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
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 4
 * Type: Minion • Uruk-Hai
 * Strength: 9
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. Skirmish: Remove (1) to make this minion strength +1 (limit +3).
 */
public class Card1_153 extends AbstractMinion {
    public Card1_153() {
        super(4, 9, 2, 5, Culture.ISENGARD, "Uruk Slayer", "1_153");
        addKeyword(Keyword.URUK_HAI);
        addKeyword(Keyword.DAMAGE);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();
        appendPlayMinionAction(actions, game, self);

        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self, 1)) {
            final CostToEffectAction action = new CostToEffectAction(self, Keyword.SKIRMISH, "Remove (1) to make this minion strength +1 (limit +3).");
            action.addCost(new RemoveTwilightEffect(1));
            action.addEffect(
                    new CheckLimitEffect(action, self, 3, Phase.SKIRMISH, false,
                            new AddUntilEndOfPhaseModifierEffect(
                                    new StrengthModifier(self, Filters.sameCard(self), 1), Phase.SKIRMISH)));

            actions.add(action);
        }

        return actions;
    }
}
