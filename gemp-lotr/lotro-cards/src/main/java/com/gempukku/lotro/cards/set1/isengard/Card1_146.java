package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
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
 * Type: Minion � Uruk-Hai
 * Strength: 8
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. Maneuver: Spot 5 companions to make this minion fierce until the regroup phase.
 */
public class Card1_146 extends AbstractMinion {
    public Card1_146() {
        super(3, 8, 2, 5, Culture.ISENGARD, "Uruk Fighter", "1_146");
        addKeyword(Keyword.URUK_HAI);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame lotroGame, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        appendPlayMinionAction(actions, lotroGame, self);

        if (lotroGame.getGameState().getCurrentPhase() == Phase.MANEUVER
                && Filters.countActive(lotroGame.getGameState(), lotroGame.getModifiersQuerying(), Filters.type(CardType.COMPANION)) >= 5) {

            CostToEffectAction action = new CostToEffectAction(self, "Spot 5 companions to make this minion fierce until the regroup phase");
            action.addEffect(new AddUntilStartOfPhaseModifierEffect(new KeywordModifier(self, Filters.sameCard(self), Keyword.FIERCE), Phase.REGROUP));

            actions.add(action);
        }

        return actions;
    }
}
