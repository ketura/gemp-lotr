package com.gempukku.lotro.cards.set17.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 8
 * Type: Minion • Man
 * Strength: 17
 * Vitality: 4
 * Site: 4
 * Game Text: Damage +1. Fierce. If you play Stampeding Chief from your hand, remove (3) and spot another [MEN] minion
 * or discard Stampeding Chief.
 */
public class Card17_049 extends AbstractMinion {
    public Card17_049() {
        super(8, 17, 4, 4, Race.MAN, Culture.MEN, "Stampeding Chief", true);
        addKeyword(Keyword.DAMAGE, 1);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.playedFromZone(game, effectResult, Zone.HAND, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            List<Effect> possibleEffects = new LinkedList<Effect>();
            if (PlayConditions.canSpot(game, Filters.not(self), Culture.MEN, CardType.MINION))
                possibleEffects.add(
                        new RemoveTwilightEffect(3));
            possibleEffects.add(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChoiceEffect(action, self.getOwner(), possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
