package com.gempukku.lotro.cards.set10.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 3
 * Site: 6
 * Game Text: Shadow: Spot 4 Free Peoples cultures and exert another [SAURON] minion twice to add (3) and draw a card.
 */
public class Card10_093 extends AbstractMinion {
    public Card10_093() {
        super(3, 9, 3, 6, Race.ORC, Culture.SAURON, "Mordor Wretch");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && GameUtils.getSpottableCulturesCount(game.getGameState(), game.getModifiersQuerying(), Side.FREE_PEOPLE) >= 4
                && PlayConditions.canExert(self, game, 2, Filters.not(self), Culture.SAURON, CardType.MINION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.not(self), Culture.SAURON, CardType.MINION));
            action.appendEffect(
                    new AddTwilightEffect(self, 3));
            action.appendEffect(
                    new DrawCardEffect(playerId, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
