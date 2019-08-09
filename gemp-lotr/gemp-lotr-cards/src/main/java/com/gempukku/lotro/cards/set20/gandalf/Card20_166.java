package com.gempukku.lotro.cards.set20.gandalf;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.PreventCardEffect;
import com.gempukku.lotro.logic.modifiers.VitalityModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Mithrandir's Touch
 * Gandalf	Condition • Companion
 * 1
 * To play, spot Gandalf.
 * Bearer must be a unbound companion (except Gandalf).
 * Response: If bearer is about to take a wound in a skirmish, discard 2 [Gandalf] cards form hand to prevent that wound.
 */
public class Card20_166 extends AbstractAttachable {
    public Card20_166() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 1, Culture.GANDALF, null, "Mithrandir's Touch", null, true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, Filter additionalAttachmentFilter, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, additionalAttachmentFilter, twilightModifier)
                && PlayConditions.canSpot(game, Filters.gandalf);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.unboundCompanion, Filters.not(Filters.gandalf));
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new VitalityModifier(self, Filters.hasAttached(self), 1));
}

    @Override
    public List<? extends Action> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Filters.hasAttached(self))
                && PlayConditions.isPhase(game, Phase.SKIRMISH)
                && PlayConditions.canDiscardFromHand(game, playerId, 2, Culture.GANDALF)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 2, Culture.GANDALF));
            action.appendEffect(
                    new PreventCardEffect((WoundCharactersEffect) effect, self.getAttachedTo()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
