package com.gempukku.lotro.cards.set19.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndRemoveFromTheGameCardsInDiscardEffect;
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
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 8
 * Game Text: Fellowship. Skirmish: If Merry is not assigned to a skirmish, take 2 [SHIRE] cards from your discard pile
 * and remove them from the game to make a companion strength +2.
 */
public class Card19_029 extends AbstractCompanion {
    public Card19_029() {
        super(1, 3, 4, 8, Culture.SHIRE, Race.HOBBIT, null, "Merry", true);
        addKeyword(Keyword.FELLOWSHIP);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSpot(game, self, Filters.notAssignedToSkirmish)
                && PlayConditions.canRemoveFromDiscard(self, game, playerId, 2, Culture.SHIRE)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndRemoveFromTheGameCardsInDiscardEffect(action, self, playerId, 2, 2, Culture.SHIRE));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
