package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Tale. Bearer must be a [GONDOR] Man. Limit 1 per bearer. Bearer is strength +1 for each Elf you can spot
 * (limit +3).
 */
public class Card1_049 extends AbstractAttachable {
    public Card1_049() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 1, Culture.ELVEN, "The Last Alliance of Elves and Men", "1_49");
        addKeyword(Keyword.TALE);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        Filter validTargetFilter = Filters.and(Filters.culture(Culture.GONDOR), Filters.keyword(Keyword.MAN), Filters.not(Filters.attached(Filters.name("The Last Alliance of Elves and Men"))));

        appendAttachCardAction(actions, game, self, validTargetFilter);

        return actions;
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new AbstractModifier(self, "Streng +1 for each Elf you can spot (limit +3)", Filters.attachedTo(self)) {
            @Override
            public int getStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
                int count = Math.min(3, Filters.countActive(gameState, modifiersQuerying, Filters.keyword(Keyword.ELF)));
                return result + count;
            }
        };
    }
}
