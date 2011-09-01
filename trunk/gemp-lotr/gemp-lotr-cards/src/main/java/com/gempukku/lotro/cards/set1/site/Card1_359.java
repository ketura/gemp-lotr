package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.cards.modifiers.MoveLimitModifier;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 9
 * Type: Site
 * Site: 8
 * Game Text: River. Shadow: Spot 5 Orc minions to prevent the fellowship from moving again this turn.
 */
public class Card1_359 extends AbstractSite {
    public Card1_359() {
        super("Shores of Nen Hithoel", 8, 9, Direction.RIGHT);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.SHADOW, self)
                && Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ORC)) >= 5) {
            CostToEffectAction action = new CostToEffectAction(self, Keyword.SHADOW, "Prevent the fellowship from moving again this turn.");
            action.addEffect(
                    new AddUntilEndOfTurnModifierEffect(
                            new MoveLimitModifier(self, -1000)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
