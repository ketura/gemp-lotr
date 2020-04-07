package com.gempukku.lotro.cards.set12.uruk_hai;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be an [URUK-HAI] minion. When you play this possession on a minion that has twilight cost of
 * 4 or more, draw a card.
 */
public class Card12_159 extends AbstractAttachable {
    public Card12_159() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.URUK_HAI, PossessionClass.HAND_WEAPON, "Weapon of Opportunity");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.URUK_HAI, CardType.MINION);
    }

    @Override
    public int getStrength() {
        return 2;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.playedOn(game, effectResult,
                new Filter() {
                    @Override
                    public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                        return physicalCard.getBlueprint().getTwilightCost() >= 4;
                    }
                }, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new DrawCardsEffect(action, self.getOwner(), 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
