package com.gempukku.lotro.cards.set7.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.PlaySiteEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Valiant. Skirmish: Replace the fellowship's site with your plains site of the same number to discard
 * a card from hand.
 */
public class Card7_233 extends AbstractCompanion {
    public Card7_233() {
        super(2, 6, 3, Culture.ROHAN, Race.MAN, null, "Grimbold", true);
        addKeyword(Keyword.VALIANT);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new PlaySiteEffect(playerId, Block.KING, game.getGameState().getCurrentSiteNumber(), Keyword.PLAINS));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
