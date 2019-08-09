package com.gempukku.lotro.cards.set40.gandalf;

import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.modifiers.CantTakeMoreThanXWoundsModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Grace of the Valar
 * Set: Second Edition
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Condition - Companion
 * Card Number: 1R74
 * Game Text: Bearer must be Gandalf.
 * Gandalf takes no more than one wound in a skirmish. If you lose initiative, discard this condition and wound Gandalf.
 */
public class Card40_074 extends AbstractAttachable{
    public Card40_074() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 1, Culture.GANDALF, null, "Grace of the Valar", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.gandalf;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        CantTakeMoreThanXWoundsModifier modifier = new CantTakeMoreThanXWoundsModifier(self, Phase.SKIRMISH, 1, Filters.hasAttached(self));
        return Collections.singletonList(modifier);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.losesInitiative(effectResult, Side.FREE_PEOPLE)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, self.getOwner(), 1, 1, self.getAttachedTo()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
