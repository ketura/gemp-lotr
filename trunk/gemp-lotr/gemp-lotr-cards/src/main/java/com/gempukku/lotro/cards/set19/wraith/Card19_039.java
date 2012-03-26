package com.gempukku.lotro.cards.set19.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.modifiers.PreventMinionBeingAssignedToCharacterModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.AssignmentResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Set: Ages End
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 6
 * Type: Minion • Nazgul
 * Strength: 12
 * Vitality: 3
 * Site: 3
 * Game Text: Fierce. Ulaire Toldea cannot be assigned to skirmish the same companion in a fierce skirmish that he has
 * already been assigned to this turn.
 */
public class Card19_039 extends AbstractMinion {
    public Card19_039() {
        super(6, 12, 3, 3, Race.NAZGUL, Culture.WRAITH, "Úlairë Toldëa", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public Modifier getAlwaysOnModifier(final PhysicalCard self) {
        return new PreventMinionBeingAssignedToCharacterModifier(self, null,
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        final Set<Integer> data = (Set<Integer>) self.getWhileInZoneData();
                        if (data == null)
                            return false;
                        return data.contains(physicalCard.getCardId());
                    }
                },
                new Condition() {
                    @Override
                    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                        return gameState.isFierceSkirmishes();
                    }
                }, self
        );
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.assignedAgainst(game, effectResult, null, Filters.any, self)) {
            Set<Integer> data = (Set<Integer>) self.getData();
            if (data == null) {
                data = new HashSet<Integer>();
                self.setWhileInZoneData(data);
            }
            for (PhysicalCard physicalCard : ((AssignmentResult) effectResult).getAgainst())
                data.add(physicalCard.getCardId());
        }
        return null;
    }
}
