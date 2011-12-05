package com.gempukku.lotro.cards.set11.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.PutCardFromPlayOnTopOfDeckEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 7
 * Vitality: 1
 * Site: 4
 * Game Text: Each time this minion is about to be killed or discarded from play, you may spot another [ORC] card
 * to place this minion on top of your draw deck instead.
 */
public class Card11_134 extends AbstractMinion {
    public Card11_134() {
        super(2, 7, 1, 4, Race.ORC, Culture.ORC, "Persistent Orc");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if ((TriggerConditions.isGettingKilled(effect, game, self) || TriggerConditions.isGettingDiscarded(effect, game, self))
                && PlayConditions.canSpot(game, Filters.not(self), Culture.ORC)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new PutCardFromPlayOnTopOfDeckEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
