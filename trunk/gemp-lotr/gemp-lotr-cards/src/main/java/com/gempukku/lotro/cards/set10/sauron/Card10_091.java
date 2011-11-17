package com.gempukku.lotro.cards.set10.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 3
 * Site: 6
 * Game Text: Shadow: If you cannot spot another minion and there are 3 or fewer twilight tokens, add (3) (or (5)
 * if this minion is roaming).
 */
public class Card10_091 extends AbstractMinion {
    public Card10_091() {
        super(3, 9, 3, 6, Race.ORC, Culture.SAURON, "Mordor Fiend");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && !PlayConditions.canSpot(game, Filters.not(self), CardType.MINION)
                && game.getGameState().getTwilightPool() <= 3) {
            ActivateCardAction action = new ActivateCardAction(self);
            int count = game.getModifiersQuerying().hasKeyword(game.getGameState(), self, Keyword.ROAMING) ? 5 : 3;
            action.appendEffect(
                    new AddTwilightEffect(self, count));
            return Collections.singletonList(action);
        }
        return null;
    }
}