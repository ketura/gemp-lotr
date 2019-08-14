package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ResistanceModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 1
 * Phial of Galadriel, Light in Dark Places
 * Elven	Artifact
 * 2
 * To play, exert an Elf (or spot Galadriel).
 * Bearer must be a Ring-bound companion.
 * Fellowship: Discard this artfiact to heal bearer (and remove a burden if bearer is the Ring-bearer).
 */
public class Card20_101 extends AbstractAttachableFPPossession {
    public Card20_101() {
        super(1, 0, 0, Culture.ELVEN, CardType.ARTIFACT, null, "Phial of Galadriel", "Light in Dark Places", true);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return (PlayConditions.canSpot(game, Filters.galadriel) || PlayConditions.canExert(self, game, Race.ELF));
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(CardType.COMPANION, Keyword.RING_BOUND);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ResistanceModifier(self, Filters.hasAttached(self), 2));
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new AbstractExtraPlayCostModifier(self, "Extra cost to play", self) {
                    @Override
                    public boolean canPayExtraCostsToPlay(LotroGame game, PhysicalCard card) {
                        return (PlayConditions.canSpot(game, Filters.galadriel) || PlayConditions.canExert(self, game, Race.ELF));
                    }

                    @Override
                    public void appendExtraCosts(LotroGame game, CostToEffectAction action, PhysicalCard card) {
                        List<Effect> possibleCosts = new LinkedList<Effect>();
                        possibleCosts.add(
                                new SpotEffect(1, Filters.galadriel));
                        possibleCosts.add(
                                new ChooseAndExertCharactersEffect(action, card.getOwner(), 1, 1, Race.ELF) {
                                    @Override
                                    public String getText(LotroGame game) {
                                        return "Exert an Elf";
                                    }
                                });
                        action.appendCost(
                                new ChoiceEffect(action, card.getOwner(), possibleCosts));
                    }
                });
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new HealCharactersEffect(self, self.getAttachedTo()));
            if (Filters.ringBearer.accepts(game, self.getAttachedTo()))
                action.appendEffect(
                        new RemoveBurdenEffect(playerId, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
