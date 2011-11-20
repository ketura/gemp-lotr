package com.gempukku.lotro.cards.set7.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Signet: Aragorn
 * Game Text: Archer. Archery: If you have initiative, discard 4 cards from hand to wound a minion.
 */
public class Card7_025 extends AbstractCompanion {
    public Card7_025() {
        super(2, 6, 3, 6, Culture.ELVEN, Race.ELF, Signet.ARAGORN, "Legolas", true);
        addKeyword(Keyword.ARCHER);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.ARCHERY, self)
                && PlayConditions.hasInitiative(game, Side.FREE_PEOPLE)
                && PlayConditions.canDiscardFromHand(game, playerId, 4, Filters.any)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 4));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
