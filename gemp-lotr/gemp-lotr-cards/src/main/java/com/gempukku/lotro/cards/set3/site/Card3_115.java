package com.gempukku.lotro.cards.set3.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Set: Realms of Elf-lords
 * Twilight Cost: 3
 * Type: Site
 * Site: 6
 * Game Text: Forest. Sanctuary. While the fellowship is at Caras Galadhon, no more than one minion may be assigned
 * to each skirmish.
 */
public class Card3_115 extends AbstractSite {
    public Card3_115() {
        super("Caras Galadhon", Block.FELLOWSHIP, 6, 3, Direction.LEFT);
        addKeyword(Keyword.FOREST);
        addKeyword(Keyword.SANCTUARY);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new AbstractModifier(self, "No more than one minion may be assigned to each skirmish", null, new ModifierEffect[]{ModifierEffect.ASSIGNMENT_MODIFIER}) {
                    @Override
                    public boolean isValidAssignments(GameState gameState, Side side, ModifiersQuerying modifiersQuerying, Map<PhysicalCard, List<PhysicalCard>> assignments, boolean result) {
                        for (Map.Entry<PhysicalCard, List<PhysicalCard>> minionsAssignedToCharacter : assignments.entrySet()) {
                            PhysicalCard fp = minionsAssignedToCharacter.getKey();
                            List<PhysicalCard> minions = minionsAssignedToCharacter.getValue();
                            List<Skirmish> skirmishes = gameState.getAssignments();
                            if (countMinionsCurrentlyAssignedToFPChar(skirmishes, fp) + minions.size() > 1)
                                return false;
                        }
                        return result;
                    }

                    private int countMinionsCurrentlyAssignedToFPChar(List<Skirmish> skirmishes, PhysicalCard fp) {
                        for (Skirmish skirmish : skirmishes) {
                            if (skirmish.getFellowshipCharacter() == fp)
                                return skirmish.getShadowCharacters().size();
                        }
                        return 0;
                    }
                });
    }
}
