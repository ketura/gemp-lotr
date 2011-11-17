package com.gempukku.lotro.cards.set10.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 4
 * Type: Minion • Man
 * Strength: 9
 * Vitality: 2
 * Site: 4
 * Game Text: Corsair. When you play this minion, you may spot another corsair and discard the top card of the Free
 * Peoples player's draw deck to add 2 [RAIDER] tokens to a card that already has a [RAIDER] token on it.
 */
public class Card10_039 extends AbstractMinion {
    public Card10_039() {
        super(4, 9, 2, 4, Race.MAN, Culture.RAIDER, "Corsair Ruffian");
        addKeyword(Keyword.CORSAIR);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, Filters.not(self), Keyword.CORSAIR)
                && game.getModifiersQuerying().canDiscardCardsFromTopOfDeck(game.getGameState(), game.getGameState().getCurrentPlayerId(), self)
                && game.getGameState().getDeck(game.getGameState().getCurrentPlayerId()).size() > 0) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new DiscardTopCardFromDeckEffect(self, game.getGameState().getCurrentPlayerId(), true));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose card", Filters.hasToken(Token.RAIDER)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new AddTokenEffect(self, card, Token.RAIDER, 2));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
