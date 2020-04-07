package com.gempukku.lotro.cards.set13.men;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.condition.NotCondition;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 6
 * Type: Minion • Man
 * Strength: 13
 * Vitality: 3
 * Site: 4
 * Game Text: When you play this, you may spot another [MEN] minion to make this minion twilight cost -1 for each wound
 * on the Ring-bearer. While each companion is wounded, this minion is fierce.
 */
public class Card13_088 extends AbstractMinion {
    public Card13_088() {
        super(6, 13, 3, 4, Race.MAN, Culture.MEN, "Dunlending Patriarch");
    }

    @Override
    public int getPotentialDiscount(LotroGame game, String playerId, PhysicalCard self) {
        if (Filters.canSpot(game, Filters.not(self), Culture.MEN, CardType.MINION))
            return game.getGameState().getWounds(game.getGameState().getRingBearer(game.getGameState().getCurrentPlayerId()));
        return 0;
    }

    @Override
    public void appendPotentialDiscountEffects(LotroGame game, CostToEffectAction action, String playerId, PhysicalCard self) {
        if (Filters.canSpot(game, Filters.not(self), Culture.MEN, CardType.MINION)) {
            int wounds = game.getGameState().getWounds(game.getGameState().getRingBearer(game.getGameState().getCurrentPlayerId()));
            if (wounds > 0)
                action.appendPotentialDiscount(new DiscountChoiceEffect(playerId, wounds));
        }
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(new KeywordModifier(self, self, new NotCondition(new SpotCondition(CardType.COMPANION, Filters.unwounded)), Keyword.FIERCE, 1));
    }
}
