package com.gempukku.lotro.cards.set11.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Twilight Cost: 0
 * Type: Site
 * Game Text: Forest. When the fellowship moves to this site, add (1) for each Free Peoples weapon.
 */
public class Card11_239 extends AbstractNewSite {
    public Card11_239() {
        super("Fangorn Glade", 0, Direction.LEFT);
        addKeyword(Keyword.FOREST);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            int weapons = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Side.FREE_PEOPLE, Filters.weapon);
            action.appendEffect(
                    new AddTwilightEffect(self, weapons));
            return Collections.singletonList(action);
        }
        return null;
    }
}
