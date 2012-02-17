package com.gempukku.lotro.cards.set14.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Expanded Middle-earth
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 3
 * Type: Minion • Half•troll
 * Strength: 8
 * Vitality: 2
 * Site: 4
 * Game Text: At the start of each assignment phase, if you can spot more companions than minions, you may remove (2)
 * to play an [ORC] Orc from your discard pile.
 */
public class Card14_012 extends AbstractMinion {
    public Card14_012() {
        super(3, 8, 2, 4, Race.HALF_TROLL, Culture.ORC, "Half-troll of Far Harad");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.ASSIGNMENT)
                && Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION)
                > Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.MINION)
                && PlayConditions.canPlayFromDiscard(playerId, game, 2, 0, Culture.ORC, Race.ORC)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(2));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.ORC, Race.ORC));
            return Collections.singletonList(action);
        }
        return null;
    }
}
