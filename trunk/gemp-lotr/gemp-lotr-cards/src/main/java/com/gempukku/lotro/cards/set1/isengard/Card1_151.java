package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Minion • Uruk-Hai
 * Strength: 5
 * Vitality: 3
 * Site: 5
 * Game Text: Damage +1.
 */
public class Card1_151 extends AbstractMinion {
    public Card1_151() {
        super(2, 5, 3, 5, Culture.ISENGARD, "Uruk Savage");
        addKeyword(Keyword.URUK_HAI);
        addKeyword(Keyword.DAMAGE);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();
        appendPlayMinionAction(actions, game, self);
        return actions;
    }
}
