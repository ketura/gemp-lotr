package com.gempukku.lotro.cards.set19.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.modifiers.PreventMinionBeingAssignedToCharacterModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Set: Ages End
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 4
 * Type: Minion • Nazgul
 * Strength: 9
 * Vitality: 2
 * Site: 3
 * Game Text: Fierce. When you play Ulaire Nertea, name a race. The Free Peoples player cannot assign Ulaire Nertea
 * to skirmish a companion of the named race.
 */
public class Card19_038 extends AbstractMinion {
    public Card19_038() {
        super(4, 9, 2, 3, Race.NAZGUL, Culture.WRAITH, "Úlairë Nertëa", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public Modifier getAlwaysOnModifier(final PhysicalCard self) {
        return new PreventMinionBeingAssignedToCharacterModifier(self, Side.FREE_PEOPLE,
                Filters.and(CardType.COMPANION, new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        final Race race = physicalCard.getBlueprint().getRace();
                        return self.getWhileInZoneData() != null && self.getWhileInZoneData() == race;
                    }
                }), self);
    }

    @Override
    public String getDisplayableInformation(PhysicalCard self) {
        if (self.getWhileInZoneData() != null)
            return "Chosen race is " + ((Race) self.getWhileInZoneData()).getHumanReadable();
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);

            final Set<String> possibleRaces = new LinkedHashSet<String>();
            for (Race race : Race.values())
                possibleRaces.add(race.getHumanReadable());

            action.appendEffect(
                    new PlayoutDecisionEffect(self.getOwner(),
                            new MultipleChoiceAwaitingDecision(1, "Choose a race", possibleRaces.toArray(new String[possibleRaces.size()])) {
                                @Override
                                protected void validDecisionMade(int index, String result) {
                                    self.setWhileInZoneData(Race.findRaceByHumanReadable(result));
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
