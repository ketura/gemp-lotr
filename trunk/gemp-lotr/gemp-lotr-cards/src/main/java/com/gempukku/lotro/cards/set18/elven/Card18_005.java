package com.gempukku.lotro.cards.set18.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Possession • Hand Weapon
 * Strength: +1
 * Game Text: Bearer must be an [ELVEN] companion. Bearer may bear this hand weapon in addition to 1 other hand weapon.
 * Maneuver: If bearer is Arwen, exert her to draw 2 cards.
 */
public class Card18_005 extends AbstractAttachableFPPossession {
    public Card18_005() {
        super(2, 1, 0, Culture.ELVEN, PossessionClass.HAND_WEAPON, "Arwen's Dagger", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.ELVEN, CardType.COMPANION);
    }

    @Override
    public boolean isExtraPossessionClass(LotroGame game, PhysicalCard self, PhysicalCard attachedTo) {
        return true;
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canExert(self, game, Filters.hasAttached(self), Filters.arwen)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self.getAttachedTo()));
            action.appendEffect(
                    new DrawCardsEffect(action, playerId, 2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
