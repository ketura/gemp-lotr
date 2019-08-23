package com.gempukku.lotro.cards.set20.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.CantTakeMoreThanXWoundsModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.OverwhelmedByMultiplierModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * •Mithril Coat, A Kingly Gift
 * Shire	Artifact • Armor
 * 1
 * Bearer must be Frodo.
 * Frodo takes no more than one wound in a skirmish.
 * Skirmish: Exert Frodo twice to prevent him from being overwhelmed unless his strength is tripled.
 */
public class Card20_399 extends AbstractAttachableFPPossession {
    public Card20_399() {
        super(2, 0, 0, Culture.SHIRE, CardType.ARTIFACT, PossessionClass.ARMOR, "Mithril Coat", "A Kingly Gift", true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.frodo;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new CantTakeMoreThanXWoundsModifier(self, Phase.SKIRMISH, 1, Filters.hasAttached(self)));
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game, 2, Filters.hasAttached(self))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.hasAttached(self)));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new OverwhelmedByMultiplierModifier(self, self.getAttachedTo(), 3)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
