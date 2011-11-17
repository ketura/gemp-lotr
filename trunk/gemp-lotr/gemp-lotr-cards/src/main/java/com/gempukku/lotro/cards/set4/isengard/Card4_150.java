package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.DoesNotAddToArcheryTotalModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 7
 * Type: Minion • Uruk-Hai
 * Strength: 9
 * Vitality: 3
 * Site: 5
 * Game Text: Archer. Damage +1. Archery: Exert Elite Crossbowmen to make the Free Peoples player wound an unbound
 * companion; this minion does not add to the minion archery total.
 */
public class Card4_150 extends AbstractMinion {
    public Card4_150() {
        super(7, 9, 3, 5, Race.URUK_HAI, Culture.ISENGARD, "Elite Crossbowmen", true);
        addKeyword(Keyword.ARCHER);
        addKeyword(Keyword.DAMAGE);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ARCHERY, self, 0)
                && PlayConditions.canExert(self, game, Filters.sameCard(self))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, Filters.unboundCompanion));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new DoesNotAddToArcheryTotalModifier(self, Filters.sameCard(self)), Phase.ARCHERY));
            return Collections.singletonList(action);
        }
        return null;
    }
}
