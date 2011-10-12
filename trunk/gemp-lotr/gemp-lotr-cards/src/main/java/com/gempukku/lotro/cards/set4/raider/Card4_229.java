package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 1
 * Type: Minion • Man
 * Strength: 5
 * Vitality: 1
 * Site: 4
 * Game Text: Easterling. When you play this minion, you may spot another Easterling to add (1) for each
 * burden (limit (4)).
 */
public class Card4_229 extends AbstractMinion {
    public Card4_229() {
        super(1, 5, 1, 4, Race.MAN, Culture.RAIDER, "Easterling Skirmisher");
        addKeyword(Keyword.EASTERLING);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game, effectResult, Filters.sameCard(self))
                && PlayConditions.canSpot(game, Filters.not(Filters.sameCard(self)), Filters.keyword(Keyword.EASTERLING))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            int twilight = Math.min(4, game.getGameState().getBurdens());

            action.appendEffect(
                    new AddTwilightEffect(self, twilight));
            return Collections.singletonList(action);
        }
        return null;
    }
}
