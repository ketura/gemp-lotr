package com.gempukku.lotro.cards.set19.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ages End
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 7
 * Game Text: Valiant. Fellowship: Play a [ROHAN] companion with a twilight cost of 3 or more; his or her twilight
 * cost is -1.
 */
public class Card19_026 extends AbstractCompanion {
    public Card19_026() {
        super(2, 6, 3, 7, Culture.ROHAN, Race.MAN, null, Names.eowyn, "Lady of the Mark", true);
        addKeyword(Keyword.VALIANT);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canPlayFromHand(playerId, game, -1, Culture.ROHAN, CardType.COMPANION, Filters.minPrintedTwilightCost(3))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, -1, Culture.ROHAN, CardType.COMPANION, Filters.minPrintedTwilightCost(3)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
