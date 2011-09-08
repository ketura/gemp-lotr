package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.List;
import java.util.Map;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Assignment: Spot 5 [MORIA] minions to make the Free Peoples player assign the Ring-bearer to a skirmish.
 */
public class Card1_169 extends AbstractEvent {
    public Card1_169() {
        super(Side.SHADOW, Culture.MORIA, "The End Comes", Phase.ASSIGNMENT);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.MORIA), Filters.type(CardType.MINION)) >= 5;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.addEffect(
                new AddUntilEndOfPhaseModifierEffect(
                        new AbstractModifier(self, "Ring bearer has to be assigned to a skirmish", Filters.keyword(Keyword.RING_BEARER), new ModifierEffect[]{ModifierEffect.ASSIGNMENT_MODIFIER}) {
                            @Override
                            public boolean isValidFreePlayerAssignments(GameState gameState, ModifiersQuerying modifiersQuerying, Map<PhysicalCard, List<PhysicalCard>> assignments, boolean result) {
                                PhysicalCard ringBearer = Filters.findFirstActive(gameState, modifiersQuerying, Filters.keyword(Keyword.RING_BEARER));
                                List<PhysicalCard> assignedToRingBearer = assignments.get(ringBearer);
                                if (assignedToRingBearer == null || assignedToRingBearer.size() == 0)
                                    return false;
                                return result;
                            }
                        }, Phase.ASSIGNMENT));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
