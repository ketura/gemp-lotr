package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 5
 * Type: Minion • Man
 * Strength: 10
 * Vitality: 3
 * Site: 4
 * Game Text: Southron. Ambush (1). Maneuver: Exert this minion and spot 5 companions to make the Free Peoples player
 * exert a companion.
 */
public class Card4_251 extends AbstractMinion {
    public Card4_251() {
        super(5, 10, 3, 4, Race.MAN, Culture.RAIDER, "Southron Fighter");
        addKeyword(Keyword.SOUTHRON);
        addKeyword(Keyword.AMBUSH, 1);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.MANEUVER, self, 0)
                && PlayConditions.canExert(self, game, Filters.sameCard(self))
                && PlayConditions.canSpot(game, 5, Filters.type(CardType.COMPANION))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, Filters.type(CardType.COMPANION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
