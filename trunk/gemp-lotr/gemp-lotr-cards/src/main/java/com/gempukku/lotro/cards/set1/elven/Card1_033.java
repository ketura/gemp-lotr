package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Possession � Ranged Weapon
 * Strength: +1
 * Game Text: Bearer must be Legolas. Each time Legolas wins a skirmish, you may wound a minion.
 */
public class Card1_033 extends AbstractAttachableFPPossession {
    public Card1_033() {
        super(1, Culture.ELVEN, Keyword.RANGED_WEAPON, "Bow of the Galadhrim", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Legolas");
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new StrengthModifier(self, Filters.isAttachedTo(self), 1);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.winsSkirmish(effectResult, self.getAttachedTo())) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self, null, "Wound a minion");
            action.addCost(
                    new ChooseActiveCardEffect(playerId, "Choose a minion", Filters.type(CardType.MINION)) {
                        @Override
                        protected void cardSelected(PhysicalCard minion) {
                            action.addEffect(new WoundCharacterEffect(playerId, minion));
                        }
                    }
            );
            return Collections.singletonList(action);
        }
        return null;
    }
}
