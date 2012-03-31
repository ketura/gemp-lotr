package com.gempukku.lotro.cards.set11.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ReplaceInSkirmishEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 9
 * Game Text: Skirmish: If Pippin is not assigned to a skirmish, spot an unbound companion who has less resistance than
 * Pippin to have Pippin replace him or her in a skirmish.
 */
public class Card11_170 extends AbstractCompanion {
    public Card11_170() {
        super(1, 3, 4, 9, Culture.SHIRE, Race.HOBBIT, null, "Pippin", "Brave Decoy", true);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && Filters.notAssignedToSkirmish.accepts(game.getGameState(), game.getModifiersQuerying(), self)) {
            final int pippinResistance = game.getModifiersQuerying().getResistance(game.getGameState(), self);
            if (PlayConditions.canSpot(game, Filters.unboundCompanion, Filters.maxResistance(pippinResistance - 1), Filters.inSkirmish)) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.appendEffect(
                        new ReplaceInSkirmishEffect(self, Filters.unboundCompanion, Filters.maxResistance(pippinResistance - 1)));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
