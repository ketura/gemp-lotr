package com.gempukku.lotro.cards.set4.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.RemoveTokenEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 5
 * Vitality: 3
 * Resistance: 6
 * Game Text: To play, spot an Elf.
 * Skirmish: Exert Fereveldir and discard an [ELVEN] token from your condition to wound a minion Fereveldir
 * is skirmishing.
 */
public class Card4_067 extends AbstractCompanion {
    public Card4_067() {
        super(2, 5, 3, Culture.ELVEN, Race.ELF, null, "Fereveldir", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Race.ELF);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), self)
                && Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.owner(playerId), CardType.CONDITION, Filters.hasToken(Token.ELVEN)) > 0) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendCost(
                    new ChooseActiveCardEffect(self, playerId, "Choose condition", Filters.owner(playerId), CardType.CONDITION, Filters.hasToken(Token.ELVEN)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertCost(
                                    new RemoveTokenEffect(self, card, Token.ELVEN));
                        }
                    });
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION, Filters.inSkirmishAgainst(Filters.sameCard(self))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
