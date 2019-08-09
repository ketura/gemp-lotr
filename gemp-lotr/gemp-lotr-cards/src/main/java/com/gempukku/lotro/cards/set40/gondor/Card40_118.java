package com.gempukku.lotro.cards.set40.gondor;

import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Ring of Barahir, Heirloom of Kings
 * Set: Second Edition
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Artifact - Ring
 * Card Number: 1R118
 * Game Text: Bearer must be Aragorn.
 * Fellowship or Regroup: Discard 2 [GONDOR] cards from hand to heal an unbound companion.
 */
public class Card40_118 extends AbstractAttachableFPPossession {
    public Card40_118() {
        super(0, 0, 0, Culture.GONDOR, PossessionClass.RING, "Ring of Barahir", "Heirloom of Kings", true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.aragorn;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if ((PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                || PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self))
                && PlayConditions.canDiscardFromHand(game, playerId, 2, Culture.GONDOR)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 2, Culture.GONDOR));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, Filters.unboundCompanion));
            return Collections.singletonList(action);
        }
        return null;
    }
}
