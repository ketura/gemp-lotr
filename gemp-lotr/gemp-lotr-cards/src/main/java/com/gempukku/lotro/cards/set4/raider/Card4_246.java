package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 4
 * Type: Minion • Man
 * Strength: 9
 * Vitality: 2
 * Site: 4
 * Game Text: Southron. Each time a companion or ally loses a skirmish involving a Southron, you may remove (1)
 * to make the Free Peoples player wound a Ring-bound companion.
 */
public class Card4_246 extends AbstractMinion {
    public Card4_246() {
        super(4, 9, 2, 4, Race.MAN, Culture.RAIDER, "Southron Assassin");
        addKeyword(Keyword.SOUTHRON);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (game.getGameState().getTwilightPool() > 0
                && PlayConditions.losesSkirmishAgainst(game.getGameState(), game.getModifiersQuerying(), effectResult,
                Filters.or(Filters.type(CardType.COMPANION), Filters.type(CardType.ALLY)),
                Filters.keyword(Keyword.SOUTHRON))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(1));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, Filters.type(CardType.COMPANION), Filters.keyword(Keyword.RING_BOUND)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
