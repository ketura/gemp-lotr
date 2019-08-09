package com.gempukku.lotro.cards.set40.moria;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.CantBeAssignedToSkirmishModifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Goblin Ransacker
 * Set: Second Edition
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 4
 * Type: Minion - Goblin
 * Strength: 8
 * Vitality: 3
 * Home: 4
 * Card Number: 1R166
 * Game Text: When you play this minion, you may spot a [MORIA] Goblin and an unbound companion bearing 3 or more
 * Free Peoples cards. That companion may not be assigned to a skirmish until the regroup phase.
 */
public class Card40_166 extends AbstractMinion {
    public Card40_166() {
        super(4, 8, 3, 4, Race.GOBLIN, Culture.MORIA, "Goblin Ransacker");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        Filter attachedThreeFPCardsFilter = new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return Filters.filter(gameState.getAttachedCards(physicalCard), gameState, modifiersQuerying, Side.FREE_PEOPLE).size() >= 3;
            }
        };
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, Culture.MORIA, Race.GOBLIN, Filters.not(self))
                && PlayConditions.canSpot(game, Filters.unboundCompanion, attachedThreeFPCardsFilter)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose unbound companion", Filters.unboundCompanion, attachedThreeFPCardsFilter) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new CantBeAssignedToSkirmishModifier(self, card), Phase.REGROUP));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
