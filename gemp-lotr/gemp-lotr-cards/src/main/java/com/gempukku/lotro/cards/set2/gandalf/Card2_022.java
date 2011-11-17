package com.gempukku.lotro.cards.set2.gandalf;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.CancelSkirmishEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Artifact
 * Vitality: +1
 * Game Text: Bearer must be Gandalf. The twilight cost of each [GANDALF] spell is -1. Skirmish: Exert Gandalf twice
 * to cancel a skirmish involving him.
 */
public class Card2_022 extends AbstractAttachableFPPossession {
    public Card2_022() {
        super(2, 0, 1, Culture.GANDALF, CardType.ARTIFACT, PossessionClass.STAFF, "Gandalf's Staff", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.gandalf;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(new TwilightCostModifier(self,
                Filters.and(
                        Culture.GANDALF,
                        Filters.keyword(Keyword.SPELL)
                ), -1));
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), 2, Filters.sameCard(self.getAttachedTo()))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self.getAttachedTo()));
            action.appendCost(
                    new ExertCharactersEffect(self, self.getAttachedTo()));

            if (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.sameCard(self.getAttachedTo()), Filters.inSkirmish))
                action.appendEffect(
                        new CancelSkirmishEffect());
            return Collections.singletonList(action);
        }
        return null;
    }
}
