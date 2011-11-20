package com.gempukku.lotro.cards.set5.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 3
 * Site: 6
 * Game Text: When you play this minion, you may spot a [SAURON] condition to draw a card for each site you control
 * (limit 3).
 */
public class Card5_103 extends AbstractMinion {
    public Card5_103() {
        super(3, 9, 3, 6, Race.ORC, Culture.SAURON, "Orc Captain");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, Culture.SAURON, CardType.CONDITION)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            int sitesControlled = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.siteControlled(playerId));
            action.appendEffect(
                    new DrawCardsEffect(playerId, Math.min(3, sitesControlled)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
