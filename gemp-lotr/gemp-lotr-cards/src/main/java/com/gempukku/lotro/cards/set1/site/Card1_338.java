package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 0
 * Type: Site
 * Site: 3
 * Game Text: River. Sanctuary. The twilight cost of the first Nazgul played to Ford of Bruinen each turn is -5.
 */
public class Card1_338 extends AbstractSite {
    public Card1_338() {
        super("Ford of Bruinen", 3, 0, Direction.RIGHT);
        addKeyword(Keyword.RIVER);
        addKeyword(Keyword.SANCTUARY);
    }

    @Override
    public Modifier getAlwaysOnEffect(final PhysicalCard self) {
        return new TwilightCostModifier(self,
                Filters.and(
                        Filters.keyword(Keyword.NAZGUL),
                        new Filter() {
                            @Override
                            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                return (self.getData() == null);
                            }
                        }), -5);
    }

    @Override
    public List<? extends Action> getRequiredOneTimeActions(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.keyword(Keyword.NAZGUL))
                && game.getGameState().getCurrentSite() == self)
            self.storeData(new Object());
        if (effectResult.getType() == EffectResult.Type.END_OF_TURN
                && self.getData() != null)
            self.removeData();
        return null;
    }
}
