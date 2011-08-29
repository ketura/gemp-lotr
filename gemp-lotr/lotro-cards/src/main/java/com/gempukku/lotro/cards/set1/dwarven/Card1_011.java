package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Companion � Dwarf
 * Strength: 5
 * Vitality: 3
 * Resistance: 6
 * Game Text: To play, spot a Dwarf. While skirmishing an Orc, Farin is strength +2.
 */
public class Card1_011 extends AbstractCompanion {
    public Card1_011() {
        super(2, 5, 3, Culture.DWARVEN, "Farin", "1_11", true);
        addKeyword(Keyword.DWARF);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();
        if (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.DWARF)))
            appendPlayCompanionActions(actions, game, self);

        appendHealCompanionActions(actions, game, self);

        return actions;
    }

    @Override
    public Modifier getAlwaysOnEffect(final PhysicalCard self) {
        return new StrengthModifier(self, "Strength +2",
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        Skirmish activeSkirmish = gameState.getSkirmish();
                        return (activeSkirmish != null
                                && activeSkirmish.getFellowshipCharacter().equals(self)
                                && Filters.filter(activeSkirmish.getShadowCharacters(), gameState, modifiersQuerying, Filters.keyword(Keyword.ORC)).size() > 0);
                    }
                }, 2);
    }
}
