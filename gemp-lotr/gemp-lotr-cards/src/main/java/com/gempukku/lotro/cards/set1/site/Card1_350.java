package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 3
 * Type: Site
 * Site: 6
 * Game Text: Sanctuary. The twilight cost of the first [MORIA] Orc played each Shadow phase is -2.
 */
public class Card1_350 extends AbstractSite {
    public Card1_350() {
        super("Dimrill Dale", 6, 3, Direction.LEFT);
        addKeyword(Keyword.SANCTUARY);
    }

    @Override
    public Modifier getAlwaysOnModifier(final PhysicalCard self) {
        return new TwilightCostModifier(self,
                Filters.and(
                        Filters.culture(Culture.MORIA),
                        Filters.race(Race.ORC),
                        new Filter() {
                            @Override
                            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                return (self.getData() == null);
                            }
                        }), -2);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.and(Filters.culture(Culture.MORIA), Filters.race(Race.ORC))))
            self.storeData(new Object());
        if (effectResult.getType() == EffectResult.Type.END_OF_TURN
                && self.getData() != null)
            self.removeData();
        return null;
    }
}
