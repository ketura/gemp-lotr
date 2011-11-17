package com.gempukku.lotro.cards.set7.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 4
 * Type: Minion • Man
 * Strength: 9
 * Vitality: 2
 * Site: 4
 * Game Text: Southron. At the start of each skirmish involving this minion, you may remove (1) to add a threat.
 */
public class Card7_138 extends AbstractMinion {
    public Card7_138() {
        super(4, 9, 2, 4, Race.MAN, Culture.RAIDER, "Desert Villain");
        addKeyword(Keyword.SOUTHRON);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.SKIRMISH)
                && Filters.inSkirmish.accepts(game.getGameState(), game.getModifiersQuerying(), self)
                && game.getGameState().getTwilightPool() >= 1) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(1));
            action.appendEffect(
                    new AddThreatsEffect(playerId, self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
