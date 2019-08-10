package com.gempukku.lotro.cards.set40.moria;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: Goblin Scimitar
 * Set: Second Edition
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 0
 * Type: Possession - Hand Weapon
 * Strength: +2
 * Card Number: 1C171
 * Game Text: Bearer must be a [MORIA] Goblin. When you play this possession, you may draw a card.
 */
public class Card40_171 extends AbstractAttachable {
    public Card40_171() {
        super(Side.SHADOW, CardType.POSSESSION, 0, Culture.MORIA, PossessionClass.HAND_WEAPON, "Goblin Scimitar");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.MORIA, Race.GOBLIN);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new DrawCardsEffect(action, playerId, 1));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public int getStrength() {
        return 2;
    }
}
