package com.gempukku.lotro.cards.set31.dwarven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Possession • Shield
 * Strength: +1
 * Game Text: Bearer must be Thorin. The twilight cost of each [DWARVEN] event is -1.
 * Response: If Thorin is about to take a wound, discard this possession to prevent that wound.
 */
public class Card31_006 extends AbstractAttachableFPPossession {
    public Card31_006() {
        super(1, 1, 0, Culture.DWARVEN, PossessionClass.SHIELD, "Oakenshield", null, true);
    }

	@Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Thorin");
	}	
	
	
    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
		return Collections.singletonList(
				new TwilightCostModifier(self, Filters.and(Culture.DWARVEN, CardType.EVENT), -1));
	}

    @Override
    public List<? extends Action> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Filters.hasAttached(self))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new PreventCardEffect((WoundCharactersEffect) effect, self.getAttachedTo()));
            return Collections.singletonList(action);
        }
        return null;
    }
}