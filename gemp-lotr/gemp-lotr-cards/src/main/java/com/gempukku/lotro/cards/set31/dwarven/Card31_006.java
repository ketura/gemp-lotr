package com.gempukku.lotro.cards.set31.dwarven;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.PreventCardEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.TriggerConditions;

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
    public Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Thorin");
	}	
	
	
    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
		return Collections.singletonList(
				new TwilightCostModifier(self, Filters.and(Culture.DWARVEN, CardType.EVENT), -1));
	}

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
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