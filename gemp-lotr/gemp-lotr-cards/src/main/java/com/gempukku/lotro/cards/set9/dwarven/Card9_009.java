package com.gempukku.lotro.cards.set9.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.ExertCharactersEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Reflections
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Artifact • Ring
 * Strength: +1
 * Vitality: +1
 * Game Text: Bearer must be a Dwarf. Response: If bearer wins a skirmish, discard this artifact to exert each Orc.
 */
public class Card9_009 extends AbstractAttachableFPPossession {
    public Card9_009() {
        super(0, 1, 1, Culture.DWARVEN, CardType.ARTIFACT, PossessionClass.RING, "Ring of Retribution", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.DWARF;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.hasAttached(self))
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ExertCharactersEffect(action, self, Race.ORC));
            return Collections.singletonList(action);
        }
        return null;
    }
}
