package com.gempukku.lotro.cards.set31.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.CheckPhaseLimitPerPlayerEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
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
 * Twilight Cost: 2
 * Type: Site
 * Site: 4
 * Game Text: Underground. River. Maneuver: Play a Shadow condition or The One Ring from your draw deck
 * (limit one per player).
 */
public class Card31_048 extends AbstractSite {
    public Card31_048() {
        super("Underground Lake", Block.HOBBIT, 4, 2, Direction.RIGHT);
        addKeyword(Keyword.UNDERGROUND);
		addKeyword(Keyword.RIVER);
    }

    @Override
    public List<? extends Action> getPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.MANEUVER, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
			action.appendEffect(new CheckPhaseLimitPerPlayerEffect(action, self, "", playerId, 1, Phase.MANEUVER,
					new ChooseAndPlayCardFromDeckEffect(playerId, Filters.or(Filters.and(Side.SHADOW, CardType.CONDITION), Filters.name("The One Ring")))));
            return Collections.singletonList(action);
        }
        return null;
    }
}