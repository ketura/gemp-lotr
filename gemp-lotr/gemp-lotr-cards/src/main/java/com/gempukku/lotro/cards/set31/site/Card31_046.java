package com.gempukku.lotro.cards.set31.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.CheckTurnLimitEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Twilight Cost: 1
 * Type: Site
 * Site: 3
 * Game Text: Plains. Sanctuary. Shadow: Exert your fierce minion to play a condition from your draw deck
 * (limit one per turn).
 */
public class Card31_046 extends AbstractSite {
    public Card31_046() {
        super("Rhudaur", Block.HOBBIT, 3, 1, Direction.RIGHT);
        addKeyword(Keyword.PLAINS);
    }

    @Override
    public List<? extends Action> getPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SHADOW, self)
                && PlayConditions.canExert(self, game, Keyword.FIERCE)) {
            final ActivateCardAction action = new ActivateCardAction(self);
			action.appendCost(
					new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Keyword.FIERCE));
            action.appendEffect(
                    new CheckTurnLimitEffect(action, self, 1,
                            new ChooseAndPlayCardFromDeckEffect(playerId, CardType.CONDITION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}