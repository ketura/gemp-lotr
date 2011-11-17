package com.gempukku.lotro.cards.set7.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndRemoveCultureTokensFromCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Response: If a possession or engine is played by your opponent, remove a [GONDOR] token from your
 * condition to make that opponent remove (1). If he or she cannot, discard that possession or engine.
 */
public class Card7_107 extends AbstractCompanion {
    public Card7_107() {
        super(2, 6, 3, Culture.GONDOR, Race.MAN, null, "Iorlas", true);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Filters.not(Filters.owner(playerId)), Filters.or(CardType.POSSESSION, Keyword.ENGINE))
                && PlayConditions.canRemoveTokens(game, Token.GONDOR, 1, Filters.owner(playerId), CardType.CONDITION)) {
            PlayCardResult playResult = (PlayCardResult) effectResult;

            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, Token.GONDOR, 1, Filters.owner(playerId), CardType.CONDITION));
            int twilight = game.getGameState().getTwilightPool();
            if (twilight >= 1)
                action.appendEffect(
                        new RemoveTwilightEffect(1));
            else
                action.appendEffect(
                        new DiscardCardsFromPlayEffect(self, playResult.getPlayedCard()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
