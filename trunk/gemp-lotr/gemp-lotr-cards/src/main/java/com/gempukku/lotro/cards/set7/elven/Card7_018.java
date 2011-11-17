package com.gempukku.lotro.cards.set7.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Possession • Ranged Weapon
 * Strength: +1
 * Game Text: Bearer must be Legolas. Each time you play an [ELVEN] skirmish event during a skirmish involving Legolas,
 * you may heal him.
 */
public class Card7_018 extends AbstractAttachableFPPossession {
    public Card7_018() {
        super(1, 1, 0, Culture.ELVEN, PossessionClass.RANGED_WEAPON, "Bow of the Galadhrim", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.legolas;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Culture.ELVEN, CardType.EVENT, Keyword.SKIRMISH)
                && Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.hasAttached(self), Filters.inSkirmish) > 0) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new HealCharactersEffect(self, Filters.hasAttached(self)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
