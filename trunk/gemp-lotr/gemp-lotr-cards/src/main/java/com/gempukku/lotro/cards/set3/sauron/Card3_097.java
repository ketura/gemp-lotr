package com.gempukku.lotro.cards.set3.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
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
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Minion • Orc
 * Strength: 5
 * Vitality: 2
 * Site: 6
 * Game Text: Regroup: Exert this minion to wound a companion (except the Ring-bearer).
 */
public class Card3_097 extends AbstractMinion {
    public Card3_097() {
        super(1, 5, 2, 6, Race.ORC, Culture.SAURON, "Orc Slayer");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.REGROUP, self, 0)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), self)) {
            ActivateCardAction action = new ActivateCardAction(self, Keyword.REGROUP);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.type(CardType.COMPANION), Filters.not(Filters.keyword(Keyword.RING_BEARER))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
