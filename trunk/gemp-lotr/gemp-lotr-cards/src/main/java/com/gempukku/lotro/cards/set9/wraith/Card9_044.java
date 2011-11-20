package com.gempukku.lotro.cards.set9.wraith;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.cards.effects.ReturnCardsToHandEffect;
import com.gempukku.lotro.cards.modifiers.VitalityModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Reflections
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 0
 * Type: Artifact • Ring
 * Vitality: +1
 * Game Text: Bearer must be a Nazgul. Maneuver: Remove a burden to heal each Nazgul. Response: If a player reconciles,
 * return bearer to his owner's hand.
 */
public class Card9_044 extends AbstractAttachable {
    public Card9_044() {
        super(Side.SHADOW, CardType.ARTIFACT, 0, Culture.WRAITH, PossessionClass.RING, "Ring of Rancor", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.NAZGUL;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new VitalityModifier(self, Filters.hasAttached(self), 1));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0)
                && PlayConditions.canRemoveBurdens(game, self, 1)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveBurdenEffect(self));
            action.appendEffect(
                    new HealCharactersEffect(self, Race.NAZGUL));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Action> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.RECONCILE) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ReturnCardsToHandEffect(self, self.getAttachedTo()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
