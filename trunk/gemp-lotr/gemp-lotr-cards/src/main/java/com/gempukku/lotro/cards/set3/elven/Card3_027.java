package com.gempukku.lotro.cards.set3.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndReturnCardsToHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 0
 * Type: Artifact
 * Vitality: +1
 * Game Text: Bearer must be Elrond. Maneuver: Exert Elrond twice and spot a Shadow condition to return that condition
 * to its owner's hand.
 */
public class Card3_027 extends AbstractAttachableFPPossession {
    public Card3_027() {
        super(0, 0, 1, Culture.ELVEN, CardType.ARTIFACT, PossessionClass.RING, "Vilya", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Elrond");
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.MANEUVER, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), 2, Filters.hasAttached(self))
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.side(Side.SHADOW), CardType.CONDITION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self.getAttachedTo()));
            action.appendCost(
                    new ExertCharactersEffect(self, self.getAttachedTo()));
            action.appendEffect(
                    new ChooseAndReturnCardsToHandEffect(action, playerId, 1, 1, Filters.side(Side.SHADOW), CardType.CONDITION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
