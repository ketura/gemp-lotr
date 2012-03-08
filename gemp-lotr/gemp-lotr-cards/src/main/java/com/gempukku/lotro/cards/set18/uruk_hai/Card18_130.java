package com.gempukku.lotro.cards.set18.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Uruk-Hai
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 8
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. Skirmish: Spot 2 other [URUK-HAI] minions and remove (2) to make this minion strength +1
 * and damage +1.
 */
public class Card18_130 extends AbstractMinion {
    public Card18_130() {
        super(3, 8, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "White Hand Traveler");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 2)
                && PlayConditions.canSpot(game, 2, Filters.not(self), Culture.URUK_HAI, CardType.MINION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(2));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, self, 1), Phase.SKIRMISH));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new KeywordModifier(self, self, Keyword.DAMAGE, 1), Phase.SKIRMISH));
            return Collections.singletonList(action);
        }
        return null;
    }
}
