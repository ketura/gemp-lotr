package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.modifiers.ResistanceModifier;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

/**
 * Plateau of Gorgoroth
 * 9	9
 * Plains.
 * The Ring-bearer is resistance -1 for each companion in the dead pile
 */
public class Card20_468 extends AbstractSite {
    public Card20_468() {
        super("Plateau of Gorgoroth", SitesBlock.SECOND_ED, 9, 9, null);
        addKeyword(Keyword.PLAINS);
    }

    @Override
    public java.util.List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return java.util.Collections.singletonList(new ResistanceModifier(self, Filters.ringBearer,
                new Evaluator() {
                    @Override
                    public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                        return -1 * Filters.filter(game.getGameState().getDeadPile(game.getGameState().getCurrentPlayerId()), game, CardType.COMPANION).size();
                    }
                }));
    }
}
