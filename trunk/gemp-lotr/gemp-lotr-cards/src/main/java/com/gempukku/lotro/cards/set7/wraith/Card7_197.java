package com.gempukku.lotro.cards.set7.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 7
 * Type: Minion • Orc
 * Strength: 15
 * Vitality: 4
 * Site: 4
 * Game Text: For each Nazgul you can spot, the Free Peoples player must exert a companion to assign this minion
 * to a skirmish. Skirmish: Exert this minion to make a Nazgul or [WRAITH] Orc strength +1.
 */
public class Card7_197 extends AbstractMinion {
    public Card7_197() {
        super(7, 15, 4, 4, Race.ORC, Culture.WRAITH, "Morgul Regiment");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(
                new AbstractModifier(self, null, null, ModifierEffect.ASSIGNMENT_MODIFIER) {
                    @Override
                    public boolean isValidAssignments(GameState gameState, Side side, ModifiersQuerying modifiersQuerying, Map<PhysicalCard, List<PhysicalCard>> assignments) {
                        if (side == Side.FREE_PEOPLE) {
                            if (isGettingAssigned(assignments, self)) {
                                return Filters.countActive(gameState, modifiersQuerying, CardType.COMPANION, Filters.canExert(self)) >= Filters.countActive(gameState, modifiersQuerying, Race.NAZGUL);
                            }
                        }

                        return true;
                    }
                });
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.assigned(game, effectResult, Side.FREE_PEOPLE, Filters.any, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            int count = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Race.NAZGUL);
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, game.getGameState().getCurrentPlayerId(), count, count, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }

    private boolean isGettingAssigned(Map<PhysicalCard, List<PhysicalCard>> assignments, PhysicalCard self) {
        for (List<PhysicalCard> physicalCards : assignments.values()) {
            if (physicalCards.contains(self))
                return true;
        }
        return false;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 1, Filters.or(Race.NAZGUL, Filters.and(Culture.WRAITH, Race.ORC))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
