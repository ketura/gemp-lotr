package com.gempukku.lotro.cards.set12.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
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
 * Set: Black Rider
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 9
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. At the start of each skirmish involving this minion, you may draw a card for each wound on
 * a character it is skirmishing.
 */
public class Card12_158 extends AbstractMinion {
    public Card12_158() {
        super(3, 9, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "Vicious Uruk");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.SKIRMISH)
                && Filters.inSkirmish.accepts(game.getGameState(), game.getModifiersQuerying(), self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);

            int count = game.getGameState().getWounds(Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.inSkirmishAgainst(self)));
            action.appendEffect(
                    new DrawCardsEffect(playerId, count));
            return Collections.singletonList(action);
        }
        return null;
    }
}
