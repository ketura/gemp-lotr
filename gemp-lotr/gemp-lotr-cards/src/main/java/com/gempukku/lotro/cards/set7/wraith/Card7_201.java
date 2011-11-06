package com.gempukku.lotro.cards.set7.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 6
 * Vitality: 2
 * Site: 4
 * Game Text: While you can spot a Nazgul, the Free Peoples player must exert a companion to assign this minion to
 * a skirmish.
 */
public class Card7_201 extends AbstractMinion {
    public Card7_201() {
        super(2, 6, 2, 4, Race.ORC, Culture.WRAITH, "Morgul Spearman");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(
                new AbstractModifier(self, null, null, ModifierEffect.ASSIGNMENT_MODIFIER) {
                    @Override
                    public boolean isValidAssignments(GameState gameState, Side side, ModifiersQuerying modifiersQuerying, Map<PhysicalCard, List<PhysicalCard>> assignments) {
                        if (side == Side.FREE_PEOPLE) {
                            if (isGettingAssigned(assignments, self) && Filters.countSpottable(gameState, modifiersQuerying, Race.NAZGUL) > 0) {
                                return Filters.countActive(gameState, modifiersQuerying, CardType.COMPANION, Filters.canExert(self)) >= 1;
                            }
                        }

                        return true;
                    }
                });
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.assigned(game, effectResult, Side.FREE_PEOPLE, Filters.any, self)
                && PlayConditions.canSpot(game, Race.NAZGUL)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, CardType.COMPANION));
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
}
