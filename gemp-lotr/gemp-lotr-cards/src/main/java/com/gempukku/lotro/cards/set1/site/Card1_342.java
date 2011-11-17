package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.modifiers.MoveLimitModifier;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 0
 * Type: Site
 * Site: 3
 * Game Text: Forest. Sanctuary. While you can spot a ranger at Rivendell Waterfall, the move limit is +1 for this turn.
 */
public class Card1_342 extends AbstractSite {
    public Card1_342() {
        super("Rivendell Waterfall", Block.FELLOWSHIP, 3, 0, Direction.RIGHT);
        addKeyword(Keyword.FOREST);

    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.END_OF_TURN)
            self.removeData();
        else if (game.getGameState().getCurrentSite() == self
                && self.getData() == null
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Keyword.RANGER)) {
            self.storeData(new Object());
            game.getModifiersEnvironment().addUntilEndOfTurnModifier(
                    new MoveLimitModifier(self, 1));
        }
        return null;
    }
}
