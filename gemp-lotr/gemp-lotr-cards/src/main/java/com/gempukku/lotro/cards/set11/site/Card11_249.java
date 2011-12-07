package com.gempukku.lotro.cards.set11.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Twilight Cost: 1
 * Type: Site
 * Game Text: Marsh. When the fellowship moves to here, heal each character who has resistance 5 or more and exert each
 * other character.
 */
public class Card11_249 extends AbstractNewSite {
    public Card11_249() {
        super("Neekerbreekers' Bog", 1, Direction.RIGHT);
        addKeyword(Keyword.MARSH);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new HealCharactersEffect(self, Filters.character, Filters.minResistance(5)));
            action.appendEffect(
                    new ExertCharactersEffect(self, Filters.character, Filters.maxResistance(4)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
