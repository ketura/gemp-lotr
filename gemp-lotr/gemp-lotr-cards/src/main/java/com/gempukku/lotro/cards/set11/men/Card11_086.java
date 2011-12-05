package com.gempukku.lotro.cards.set11.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
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
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 8
 * Vitality: 2
 * Site: 4
 * Game Text: Each time the Free Peoples player assigns this minion to a skirmish, add (1) for each Free Peoples
 * character who has resistance 5 or less.
 */
public class Card11_086 extends AbstractMinion {
    public Card11_086() {
        super(3, 8, 2, 4, Race.MAN, Culture.MEN, "Invading Haradrim");
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.assignedAgainst(game, effectResult, Side.FREE_PEOPLE, Filters.any, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            int twilight = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Side.FREE_PEOPLE, Filters.character, Filters.maxResistance(5));
            action.appendEffect(
                    new AddTwilightEffect(self, twilight));
            return Collections.singletonList(action);
        }
        return null;
    }
}
