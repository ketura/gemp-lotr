package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.CheckLimitEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
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
 * Twilight Cost: 4
 * Type: Minion • Uruk-Hai
 * Strength: 9
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. Skirmish: Remove (1) to make this minion strength +1 (limit +3).
 */
public class Card1_153 extends AbstractMinion {
    public Card1_153() {
        super(4, 9, 2, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk Slayer");
        addKeyword(Keyword.DAMAGE);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self, 1)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new RemoveTwilightEffect(1));
            action.appendEffect(
                    new CheckLimitEffect(action, self, 3, Phase.SKIRMISH,
                            new AddUntilEndOfPhaseModifierEffect(
                                    new StrengthModifier(self, Filters.sameCard(self), 1), Phase.SKIRMISH)));

            return Collections.singletonList(action);
        }
        return null;
    }
}
