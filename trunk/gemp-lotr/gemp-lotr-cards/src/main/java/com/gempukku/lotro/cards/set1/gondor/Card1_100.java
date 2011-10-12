package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Tale. Plays to your support area. Maneuver: Exert Aragorn to heal Arwen, or exert Arwen to heal Aragorn.
 */
public class Card1_100 extends AbstractPermanent {
    public Card1_100() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GONDOR, Zone.SUPPORT, "The Choice of Luthien", true);
        addKeyword(Keyword.TALE);
    }

    @Override
    public List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.MANEUVER, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.or(Filters.name("Arwen"), Filters.name("Aragorn")))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.or(Filters.name("Arwen"), Filters.name("Aragorn"))) {
                        @Override
                        protected void forEachCardExertedCallback(PhysicalCard character) {
                            Filter filter;
                            if (character.getBlueprint().getName().equals("Aragorn"))
                                filter = Filters.name("Arwen");
                            else
                                filter = Filters.name("Aragorn");
                            action.appendEffect(
                                    new ChooseAndHealCharactersEffect(action, playerId, filter));
                        }
                    }
            );
            return Collections.singletonList(action);
        }
        return null;
    }
}
