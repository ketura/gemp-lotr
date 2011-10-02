package com.gempukku.lotro.cards.set3.shire;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.effects.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
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
        super(1, 1, 1, 2, Race.HOBBIT, Culture.SHIRE, "Old Noakes", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.DISCARD_FROM_HAND) {
            DiscardCardFromHandResult discardCardResult = (DiscardCardFromHandResult) effectResult;
            if (discardCardResult.getDiscardedCard().getOwner().equals(playerId)) {
                // TODO This should only work if Shadow card makes you discard the card...
                PhysicalCard source = discardCardResult.getSource();
                if (source != null && source.getBlueprint().getSide() == Side.SHADOW) {
                    OptionalTriggerAction action = new OptionalTriggerAction(self);
                    action.appendEffect(
                            new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.or(Filters.type(CardType.MINION), Filters.and(Filters.side(Side.SHADOW), Filters.type(CardType.CONDITION)))));
                    return Collections.singletonList(action);
                }
            }
        }
        return null;
    }
}
