package com.gempukku.lotro.cards.set11.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
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
 * Game Text: Battleground. When the fellowship moves to the site, add 2 for each companion over 5.
 */
public class Card11_241 extends AbstractNewSite {
    public Card11_241() {
        super("Fortress of Orthanc", 0, Direction.LEFT);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            int count = Math.max(0, Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION) - 5);
            action.appendEffect(
                    new AddTwilightEffect(self, 2 * count));
            return Collections.singletonList(action);
        }
        return null;
    }
}
