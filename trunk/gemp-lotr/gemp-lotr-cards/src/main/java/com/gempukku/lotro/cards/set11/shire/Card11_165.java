package com.gempukku.lotro.cards.set11.shire;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 4
 * Type: Condition
 * Game Text: Toil 2. (For each [SHIRE] character you exert when playing this, its twilight cost is -2.) Bearer must be
 * a Hobbit. Limit 1 per bearer. Each time the fellowship moves from a dwelling or forest site, remove (1).
 */
public class Card11_165 extends AbstractAttachable {
    public Card11_165() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 4, Culture.SHIRE, null, "Habits of Home");
        addKeyword(Keyword.TOIL, 2);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Race.HOBBIT, Filters.not(Filters.hasAttached(Filters.name(getName()))));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesFrom(game, effectResult, Filters.or(Keyword.DWELLING, Keyword.FOREST))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new RemoveTwilightEffect(1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
