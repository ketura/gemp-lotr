package com.gempukku.lotro.cards.set4.gandalf;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.MayNotBearModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Possession • Mount
 * Strength: +2
 * Game Text: Bearer must be Gandalf. Discard any hand weapon he bears. Gandalf may not bear a hand weapon.
 * At the start of each skirmish involving Gandalf, each minion skirmishing him must exert.
 */
public class Card4_100 extends AbstractAttachableFPPossession {
    public Card4_100() {
        super(2, 2, 0, Culture.GANDALF, PossessionClass.MOUNT, "Shadowfax", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.gandalf;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new MayNotBearModifier(self, Filters.hasAttached(self), PossessionClass.HAND_WEAPON));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Filters.sameCard(self))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, Filters.and(Filters.attachedTo(Filters.hasAttached(self)), PossessionClass.HAND_WEAPON)));
            return Collections.singletonList(action);
        }
        if (effectResult.getType() == EffectResult.Type.START_OF_PHASE
                && game.getGameState().getCurrentPhase() == Phase.SKIRMISH) {
            final Skirmish skirmish = game.getGameState().getSkirmish();
            if (skirmish != null && skirmish.getFellowshipCharacter() != null && skirmish.getFellowshipCharacter().getBlueprint().getName().equals("Gandalf")) {
                RequiredTriggerAction action = new RequiredTriggerAction(self);
                action.appendEffect(
                        new ExertCharactersEffect(self, Filters.inSkirmishAgainst(Filters.gandalf)));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
