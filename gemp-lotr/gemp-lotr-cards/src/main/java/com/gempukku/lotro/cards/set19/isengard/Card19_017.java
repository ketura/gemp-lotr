package com.gempukku.lotro.cards.set19.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ages End
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Weather. Each time the fellowship moves, add (2) for each Ent you can spot.
 * Skirmish: Discard this condition from play to make a non-[WRAITH] minion strength +1.
 */
public class Card19_017 extends AbstractPermanent {
    public Card19_017() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.ISENGARD, Zone.SUPPORT, "Urgency");
        addKeyword(Keyword.WEATHER);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.moves(game, effectResult)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            int count = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Race.ENT);
            action.appendEffect(
                    new AddTwilightEffect(self, 2 * count));
            return Collections.singletonList(action);
        }
        return null;
    }
}
