package com.gempukku.lotro.cards.set4.shire;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Possession • Cloak
 * Game Text: Bearer must be Frodo. Skirmish: At sites 1T to 4T, add a burden and discard this possession to cancel
 * a skirmish involving Frodo. At any other site, discard this possession to remove a burden and heal Frodo.
 */
public class Card4_303 extends AbstractAttachableFPPossession {
    public Card4_303() {
        super(2, 0, 0, Culture.SHIRE, PossessionClass.CLOAK, "Frodo's Cloak", null, true);
    }

    @Override
    public Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.frodo;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)) {
            boolean firstOption = (game.getGameState().getCurrentSiteNumber() <= 4 && game.getGameState().getCurrentSiteBlock() == SitesBlock.TWO_TOWERS);
            ActivateCardAction action = new ActivateCardAction(self);
            if (firstOption) {
                action.appendCost(
                        new AddBurdenEffect(self.getOwner(), self, 1));
                action.appendCost(
                        new SelfDiscardEffect(self));
                action.appendEffect(
                        new CancelSkirmishEffect(Filters.sameCard(self.getAttachedTo())));
            } else {
                action.appendCost(
                        new SelfDiscardEffect(self));
                action.appendEffect(
                        new RemoveBurdenEffect(playerId, self));
                action.appendEffect(
                        new HealCharactersEffect(self, self.getOwner(), self.getAttachedTo()));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
