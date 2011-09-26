package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndHealCharacterEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 8
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. Maneuver: Remove (2) to heal an Uruk-hai.
 */
public class Card1_152 extends AbstractMinion {
    public Card1_152() {
        super(3, 8, 2, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk Shaman");
        addKeyword(Keyword.DAMAGE);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.MANEUVER, self, 2)) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.MANEUVER, "Remove (2) to heal an Uruk-hai");
            action.appendCost(new RemoveTwilightEffect(2));
            action.appendEffect(
                    new ChooseAndHealCharacterEffect(action, playerId, "Choose an Uruk-hai", Filters.race(Race.URUK_HAI)));

            return Collections.singletonList(action);
        }
        return null;
    }
}
