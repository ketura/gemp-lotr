package com.gempukku.lotro.cards.set12.uruk_hai;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: While you can spot an [URUK-HAI] minion, each unbound companion is resistance -1 for each condition he or
 * she bears. Skirmish: Spot an [URUK-HAI] minion and a companion who has resistance 2 or less to discard from play all
 * conditions in all player's support areas.
 */
public class Card12_148 extends AbstractPermanent {
    public Card12_148() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.URUK_HAI, Zone.SUPPORT, "Tempest of War");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new ResistanceModifier(self, Filters.unboundCompanion, new SpotCondition(Culture.URUK_HAI, CardType.MINION),
                new Evaluator() {
                    @Override
                    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                        return -Filters.countActive(gameState, modifiersQuerying, CardType.CONDITION, Filters.attachedTo(cardAffected));
                    }
                });
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSpot(game, Culture.URUK_HAI, CardType.MINION)
                && PlayConditions.canSpot(game, CardType.COMPANION, Filters.maxResistance(2))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, CardType.CONDITION, Zone.SUPPORT));
            return Collections.singletonList(action);
        }
        return null;
    }
}
