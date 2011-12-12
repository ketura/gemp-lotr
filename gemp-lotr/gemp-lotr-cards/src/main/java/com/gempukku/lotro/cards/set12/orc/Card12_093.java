package com.gempukku.lotro.cards.set12.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ShuffleCardsFromDiscardIntoDeckEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;

import java.util.Collections;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 8
 * Vitality: 2
 * Site: 4
 * Game Text: When this minion is discarded from play, you may spot a companion who has resistance 4 or less to shuffle
 * this minion into your draw deck.
 */
public class Card12_093 extends AbstractMinion {
    public Card12_093() {
        super(3, 8, 2, 4, Race.ORC, Culture.ORC, "Orc Footman");
    }

    @Override
    public OptionalTriggerAction getDiscardedFromPlayOptionalTrigger(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canSpot(game, CardType.COMPANION, Filters.maxResistance(4))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ShuffleCardsFromDiscardIntoDeckEffect(self, playerId, Collections.singleton(self)));
            return action;
        }
        return null;
    }
}
