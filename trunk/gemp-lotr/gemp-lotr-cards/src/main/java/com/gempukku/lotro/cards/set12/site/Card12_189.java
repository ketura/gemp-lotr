package com.gempukku.lotro.cards.set12.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Twilight Cost: 2
 * Type: Site
 * Game Text: Dwelling. When the fellowship moves from this site, wound each minion that is not a lurker.
 */
public class Card12_189 extends AbstractNewSite {
    public Card12_189() {
        super("Hobbiton Market", 2, Direction.LEFT);
        addKeyword(Keyword.DWELLING);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesFrom(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new WoundCharactersEffect(self, CardType.MINION, Filters.not(Keyword.LURKER)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
