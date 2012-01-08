package com.gempukku.lotro.cards.set13.isengard;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndReturnCardsToHandEffect;
import com.gempukku.lotro.cards.modifiers.VitalityModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Artifact • Staff
 * Strength: +2
 * Vitality: +1
 * Game Text: Bearer must be a Wizard. Regroup: If Saruman is the only minion you can spot, discard this artifact from
 * play to return him to your hand.
 */
public class Card13_081 extends AbstractAttachable {
    public Card13_081() {
        super(Side.SHADOW, CardType.ARTIFACT, 2, Culture.ISENGARD, PossessionClass.STAFF, "Staff of Saruman", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.WIZARD;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 2));
        modifiers.add(
                new VitalityModifier(self, Filters.hasAttached(self), 1));
        return modifiers;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && PlayConditions.canSpot(game, Filters.saruman) && !PlayConditions.canSpot(game, 2, CardType.MINION)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndReturnCardsToHandEffect(action, playerId, 1, 1, Filters.saruman));
            return Collections.singletonList(action);
        }
        return null;
    }
}
