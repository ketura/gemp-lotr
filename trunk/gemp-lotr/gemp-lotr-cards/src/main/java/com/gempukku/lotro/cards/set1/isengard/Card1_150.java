package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
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
 * Game Text: Damage +1. Each time this minion wins a skirmish, the Free Peoples player must discard the top 2 cards of
 * his draw deck.
 */
public class Card1_150 extends AbstractMinion {
    public Card1_150() {
        super(4, 9, 2, 5, Culture.ISENGARD, "Uruk Rager");
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
    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.winsSkirmish(effectResult, self)) {
            CostToEffectAction action = new CostToEffectAction(self, null, "Free Peoples player must discard the top 2 cards of his draw deck.");
            action.addEffect(new DiscardTopCardFromDeckEffect(game.getGameState().getCurrentPlayerId()));
            action.addEffect(new DiscardTopCardFromDeckEffect(game.getGameState().getCurrentPlayerId()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
