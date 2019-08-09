package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.modifiers.SanctuaryHealModifier;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.Collections;
import java.util.List;

/**
 * Title: Great Feasting Hall
 * Set: Second Edition
 * Side: None
 * Site Number: 6
 * Shadow Number: 3
 * Card Number: 1U295
 * Game Text: Sanctuary. At the start of your turn, heal two additional wounds from companions if you can spot a companion in the dead pile.
 */
public class Card40_295 extends AbstractSite {
    public Card40_295() {
        super("Great Feasting Hall", Block.SECOND_ED, 6, 3, Direction.LEFT);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(final LotroGame game, PhysicalCard self) {
        SanctuaryHealModifier modifier = new SanctuaryHealModifier(self,
                new Condition() {
                    @Override
                    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                        return Filters.filter(gameState.getDeadPile(gameState.getCurrentPlayerId()), game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION).size() > 0;
                    }
                }, 2);
        return Collections.singletonList(modifier);
    }
}
