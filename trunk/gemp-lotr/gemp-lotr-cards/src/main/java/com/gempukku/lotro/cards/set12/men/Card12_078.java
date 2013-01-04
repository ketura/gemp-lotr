package com.gempukku.lotro.cards.set12.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 12
 * Vitality: 2
 * Site: 4
 * Game Text: Each time this minion is assigned to a skirmish, if it is not at a plains site, remove (2) or discard
 * this minion.
 */
public class Card12_078 extends AbstractMinion {
    public Card12_078() {
        super(3, 12, 2, 4, Race.MAN, Culture.MEN, "Wrathful Hillman");
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.assignedToSkirmish(game, effectResult, null, self)
                && !PlayConditions.location(game, Keyword.PLAINS)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new RemoveTwilightEffect(2));
            possibleEffects.add(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChoiceEffect(action, self.getOwner(), possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
