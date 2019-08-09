package com.gempukku.lotro.cards.set11.site;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.Assignment;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractShadowsSite;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Set: Shadows
 * Twilight Cost: 2
 * Type: Site
 * Game Text: Forest. No more than 1 minion can be assigned to each skirmish.
 */
public class Card11_231 extends AbstractShadowsSite {
    public Card11_231() {
        super("Caras Galadhon", 2, Direction.LEFT);
        addKeyword(Keyword.FOREST);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new AbstractModifier(self, "No more than one minion may be assigned to each skirmish", null, ModifierEffect.ASSIGNMENT_MODIFIER) {
                    @Override
                    public boolean isValidAssignments(LotroGame game, Side side, Map<PhysicalCard, Set<PhysicalCard>> assignments) {
                        for (Map.Entry<PhysicalCard, Set<PhysicalCard>> minionsAssignedToCharacter : assignments.entrySet()) {
                            PhysicalCard fp = minionsAssignedToCharacter.getKey();
                            Set<PhysicalCard> minions = minionsAssignedToCharacter.getValue();
                            List<Assignment> alreadyAssigned = game.getGameState().getAssignments();
                            if (countMinionsCurrentlyAssignedToFPChar(alreadyAssigned, fp) + minions.size() > 1)
                                return false;
                        }
                        return true;
                    }

                    private int countMinionsCurrentlyAssignedToFPChar(List<Assignment> assignments, PhysicalCard fp) {
                        for (Assignment assignment : assignments) {
                            if (assignment.getFellowshipCharacter() == fp)
                                return assignment.getShadowCharacters().size();
                        }
                        return 0;
                    }
                });
    }
}
