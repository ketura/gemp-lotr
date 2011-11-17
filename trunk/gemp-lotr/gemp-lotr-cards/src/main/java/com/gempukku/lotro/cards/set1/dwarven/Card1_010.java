package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Bearer must be a Dwarf. When you play this condition, heal bearer up to 2 times. At the start of each of
 * your turns, exert bearer.
 */
public class Card1_010 extends AbstractAttachable {
    public Card1_010() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 0, Culture.DWARVEN, null, "Dwarven Heart");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.DWARF;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.sameCard(self))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(new HealCharactersEffect(self, self.getAttachedTo()));
            action.appendEffect(new HealCharactersEffect(self, self.getAttachedTo()));
            return Collections.singletonList(action);
        } else if (effectResult.getType() == EffectResult.Type.START_OF_TURN) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(new ExertCharactersEffect(self, self.getAttachedTo()));
            return Collections.singletonList(action);
        }

        return null;
    }
}
