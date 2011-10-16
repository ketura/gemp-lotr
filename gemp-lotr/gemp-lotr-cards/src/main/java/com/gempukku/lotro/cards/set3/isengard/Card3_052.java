package com.gempukku.lotro.cards.set3.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Shadow: Exert Saruman to play an [ISENGARD] weather condition from your
 * discard pile. Its twilight cost is -2.
 */
public class Card3_052 extends AbstractPermanent {
    public Card3_052() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.ISENGARD, Zone.SUPPORT, "A Fell Voice on the Air");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SHADOW, self, 0)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.name("Saruman"))
                && Filters.filter(game.getGameState().getDiscard(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.ISENGARD), Filters.keyword(Keyword.WEATHER), Filters.type(CardType.CONDITION), Filters.playable(game, -2)).size() > 0) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.name("Saruman")));
            List<? extends PhysicalCard> discard = game.getGameState().getDiscard(playerId);
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, discard, -2, Filters.and(Filters.culture(Culture.ISENGARD), Filters.keyword(Keyword.WEATHER), Filters.type(CardType.CONDITION))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
