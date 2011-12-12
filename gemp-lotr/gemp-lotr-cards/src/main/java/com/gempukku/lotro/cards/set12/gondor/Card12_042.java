package com.gempukku.lotro.cards.set12.gondor;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ReplaceInSkirmishEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be Boromir. He is damage +1. Skirmish: If Boromir is not assigned to a skirmish, discard
 * a [GONDOR] card borne by him to have him replace an unbound Hobbit skirmishing a minion.
 */
public class Card12_042 extends AbstractAttachableFPPossession {
    public Card12_042() {
        super(1, 2, 0, Culture.GONDOR, PossessionClass.HAND_WEAPON, "Blade of Gondor", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.boromir;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.DAMAGE, 1));
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && Filters.notAssignedToSkirmish.accepts(game.getGameState(), game.getModifiersQuerying(), self.getAttachedTo())) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Culture.GONDOR, Filters.attachedTo(Filters.hasAttached(self))));
            action.appendEffect(
                    new ReplaceInSkirmishEffect(self, Filters.unboundCompanion, Race.HOBBIT));
            return Collections.singletonList(action);
        }
        return null;
    }
}
