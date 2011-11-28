package com.gempukku.lotro.cards.set3.shire;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardFromHandResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Ally • Home 1 • Hobbit
 * Strength: 1
 * Vitality: 2
 * Site: 1
 * Game Text: Each time a Shadow card makes you discard a card from hand, you may also discard a minion or Shadow
 * condition.
 */
public class Card3_111 extends AbstractAlly {
    public Card3_111() {
        super(1, Block.FELLOWSHIP, 1, 1, 2, Race.HOBBIT, Culture.SHIRE, "Old Noakes", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.FOR_EACH_DISCARDED_FROM_HAND) {
            DiscardCardFromHandResult discardCardsResult = (DiscardCardFromHandResult) effectResult;
            if (discardCardsResult.isForced()) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                action.appendEffect(
                        new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.or(CardType.MINION, Filters.and(Side.SHADOW, CardType.CONDITION))));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
