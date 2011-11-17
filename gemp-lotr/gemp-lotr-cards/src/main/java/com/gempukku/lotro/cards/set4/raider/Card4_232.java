package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.DoesNotAddToArcheryTotalModifier;
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
 * Twilight Cost: 4
 * Type: Minion • Man
 * Strength: 8
 * Vitality: 2
 * Site: 4
 * Game Text: Southron. Archer. Archery: Exert this minion to exert a companion (except the Ring-bearer); this minion
 * does not add to the minion archery total.
 */
public class Card4_232 extends AbstractMinion {
    public Card4_232() {
        super(4, 8, 2, 4, Race.MAN, Culture.RAIDER, "Elite Archer");
        addKeyword(Keyword.SOUTHRON);
        addKeyword(Keyword.ARCHER);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.ARCHERY, self, 0)
                && PlayConditions.canExert(self, game, Filters.sameCard(self))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.not(Filters.keyword(Keyword.RING_BEARER))));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new DoesNotAddToArcheryTotalModifier(self, Filters.sameCard(self)), Phase.ARCHERY));
            return Collections.singletonList(action);
        }
        return null;
    }
}
