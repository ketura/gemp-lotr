package com.gempukku.lotro.cards.set15.rohan;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.LiberateASiteEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.modifiers.conditions.NotCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Possession • Shield
 * Game Text: Bearer must be a [ROHAN] Man. While no opponent controls a site, bearer is strength +1.
 * Regroup: Discard this to liberate a site (or two sites if bearer is mounted).
 */
public class Card15_141 extends AbstractAttachableFPPossession {
    public Card15_141() {
        super(2, 0, 0, Culture.ROHAN, PossessionClass.SHIELD, "Sturdy Shield");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.ROHAN, Race.MAN);
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.hasAttached(self), new NotCondition(new SpotCondition(Filters.siteControlledByShadowPlayer(self.getOwner()))), 1));
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new LiberateASiteEffect(self));
            if (Filters.mounted.accepts(game.getGameState(), game.getModifiersQuerying(), self.getAttachedTo()))
                action.appendEffect(
                        new LiberateASiteEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
