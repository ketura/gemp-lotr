package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 6
 * Signet: Frodo
 * Game Text: Skirmish: If Merry is not assigned to a skirmish, exert him twice to add his strength to another
 * companion.
 */
public class Card1_302 extends AbstractCompanion {
    public Card1_302() {
        super(1, 3, 4, Culture.SHIRE, Race.HOBBIT, Signet.FRODO, "Merry", true);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), 2, Filters.sameCard(self))
                && !isAssigned(game, self)) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.SKIRMISH);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose another companion", Filters.type(CardType.COMPANION), Filters.not(Filters.sameCard(self))) {
                        @Override
                        protected void cardSelected(PhysicalCard anotherCompanion) {
                            int merryStrength = game.getModifiersQuerying().getStrength(game.getGameState(), self);
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.sameCard(anotherCompanion), merryStrength), Phase.SKIRMISH));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    private boolean isAssigned(LotroGame game, PhysicalCard self) {
        for (Skirmish skirmish : game.getGameState().getAssignments()) {
            if (skirmish.getFellowshipCharacter() == self)
                return true;
        }
        if (game.getGameState().getSkirmish().getFellowshipCharacter() == self)
            return true;

        return false;
    }
}
