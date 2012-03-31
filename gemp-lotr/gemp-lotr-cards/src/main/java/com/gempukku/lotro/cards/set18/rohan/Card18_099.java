package com.gempukku.lotro.cards.set18.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndTransferAttachableEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Valiant. Skirmish: Transfer a [ROHAN] possession that Gamling can bear to him.
 */
public class Card18_099 extends AbstractCompanion {
    public Card18_099() {
        super(2, 6, 3, 6, Culture.ROHAN, Race.MAN, null, "Gamling", "Dutiful Marshal", true);
        addKeyword(Keyword.VALIANT);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndTransferAttachableEffect(action, playerId, Filters.and(Culture.ROHAN, CardType.POSSESSION), Filters.not(self), self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
