package com.gempukku.lotro.cards.set7.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
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
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 10
 * Signet: Gandalf
 * Game Text: Ring-bearer (resistance 10). Ring-bound. Skirmish: At sites 6K to 8K, add 4 burdens to wound a minion
 * skirmishing Frodo.
 */
public class Card7_318 extends AbstractCompanion {
    public Card7_318() {
        super(0, 3, 4, 10, Culture.SHIRE, Race.HOBBIT, Signet.GANDALF, "Frodo", true);
        addKeyword(Keyword.CAN_START_WITH_RING);
        addKeyword(Keyword.RING_BOUND);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && Filters.and(Filters.siteBlock(Block.KING), Filters.or(Filters.siteNumber(6), Filters.siteNumber(7), Filters.siteNumber(8)))
                .accepts(game.getGameState(), game.getModifiersQuerying(), game.getGameState().getCurrentSite())) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new AddBurdenEffect(self, 4));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION, Filters.inSkirmishAgainst(self)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
