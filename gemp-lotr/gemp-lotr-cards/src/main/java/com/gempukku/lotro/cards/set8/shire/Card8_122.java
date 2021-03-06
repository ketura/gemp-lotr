package com.gempukku.lotro.cards.set8.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Companion • Hobbit
 * Strength: 5
 * Vitality: 4
 * Resistance: 6
 * Signet: Frodo
 * Game Text: To play, spot a [GONDOR] Man. Skirmish: Discard 4 cards from hand to wound a minion Pippin is skirmishing
 * twice if that minion bears a fortification.
 */
public class Card8_122 extends AbstractCompanion {
    public Card8_122() {
        super(2, 5, 4, 6, Culture.SHIRE, Race.HOBBIT, Signet.FRODO, "Pippin", "Guard of Minas Tirith", true);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.GONDOR, Race.MAN);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canDiscardFromHand(game, playerId, 4, Filters.any)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 4));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, 2, CardType.MINION, Filters.inSkirmishAgainst(self), Filters.hasAttached(Keyword.FORTIFICATION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
