package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.DiscardCardAtRandomFromHandEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 10
 * Vitality: 3
 * Site: 6
 * Game Text: Each time this minion wins a skirmish, the Free Peoples player discards 2 cards at random from hand.
 */
public class Card1_280 extends AbstractMinion {
    public Card1_280() {
        super(4, 10, 3, 6, Race.ORC, Culture.SAURON, "Tower Lieutenant", true);
    }

    @Override
    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.winsSkirmish(effectResult, self)) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "The Free Peoples player discards 2 cards at random from hand.");
            action.addEffect(
                    new DiscardCardAtRandomFromHandEffect(game.getGameState().getCurrentPlayerId()));
            action.addEffect(
                    new DiscardCardAtRandomFromHandEffect(game.getGameState().getCurrentPlayerId()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
