package com.gempukku.lotro.cards.set12.uruk_hai;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 4
 * Type: Minion • Uruk-Hai
 * Strength: 8
 * Vitality: 3
 * Site: 5
 * Game Text: Damage +1. Skirmish: Exert this minion to make it strength +1 for each [URUK-HAI] Uruk-hai you spot (or +2
 * for each if you can spot a companion who has resistance 3 or less).
 */
public class Card12_152 extends AbstractMinion {
    public Card12_152() {
        super(4, 8, 3, 5, Race.URUK_HAI, Culture.URUK_HAI, "Uruk Dominator", null, true);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfExert(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ForEachYouSpotEffect(playerId, Culture.URUK_HAI, Race.URUK_HAI) {
                        @Override
                        protected void spottedCards(int spotCount) {
                            int bonus = PlayConditions.canSpot(game, CardType.COMPANION, Filters.maxResistance(3)) ? 2 * spotCount : spotCount;
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, self, bonus)));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
