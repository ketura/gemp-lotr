package com.gempukku.lotro.cards.set7.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 4
 * Type: Companion • Wizard
 * Strength: 9
 * Vitality: 4
 * Resistance: 6
 * Signet: Gandalf
 * Game Text: While you cannot spot 3 companions of the same culture, Gandalf is strength -2.
 */
public class Card7_036 extends AbstractCompanion {
    public Card7_036() {
        super(4, 9, 4, 6, Culture.GANDALF, Race.WIZARD, Signet.GANDALF, "Gandalf", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, self,
                        new Condition() {
                            @Override
                            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                Set<Culture> companionCultures = new HashSet<Culture>();
                                for (PhysicalCard companion : Filters.filterActive(gameState, modifiersQuerying, CardType.COMPANION))
                                    companionCultures.add(companion.getBlueprint().getCulture());

                                for (Culture companionCulture : companionCultures) {
                                    if (Filters.countSpottable(gameState, modifiersQuerying, CardType.COMPANION, companionCulture) >= 3)
                                        return false;
                                }

                                return true;
                            }
                        }, -2));
    }
}
