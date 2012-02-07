package com.gempukku.lotro.cards.set15.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Twilight Cost: 1
 * Type: Site
 * Game Text: When the Fellowship moves to this site, add a threat (or 2 if you can spot a hunter minion)
 * for each companion over 4.
 */
public class Card15_189 extends AbstractNewSite {
    public Card15_189() {
        super("City Gates", 1, Direction.RIGHT);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            int threats = PlayConditions.canSpot(game, CardType.MINION, Keyword.HUNTER) ? 2 : 1;
            int companions = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION);
            if (companions > 4)
                action.appendEffect(
                        new AddThreatsEffect(game.getGameState().getCurrentPlayerId(), self, (companions - 4) * threats));
            return Collections.singletonList(action);
        }
        return null;
    }
}
