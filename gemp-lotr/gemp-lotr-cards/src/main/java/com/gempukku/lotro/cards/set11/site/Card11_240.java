package com.gempukku.lotro.cards.set11.site;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractShadowsSite;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Twilight Cost: 2
 * Type: Site
 * Game Text: Plains. Each unbound companion skirmishing more than 1 minion is strength +3.
 */
public class Card11_240 extends AbstractShadowsSite {
    public Card11_240() {
        super("Flats of Rohan", 2, Direction.LEFT);
        addKeyword(Keyword.PLAINS);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(new StrengthModifier(self,
                Filters.and(Filters.unboundCompanion, Filters.inSkirmish,
                        new Filter() {
                            @Override
                            public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                                return game.getGameState().getSkirmish().getShadowCharacters().size() > 1;
                            }
                        }), 3));
    }
}
