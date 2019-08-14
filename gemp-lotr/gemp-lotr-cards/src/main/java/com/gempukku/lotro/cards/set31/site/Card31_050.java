package com.gempukku.lotro.cards.set31.site;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.CantBeAssignedToSkirmishModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Forest River. Site Card.
 * Site Number: 5
 * Shadow Number: 7
 * 'Forest. River. Assigment: Exert an Orc to prevent Gandalf or an [Elven] ally from being assigned to a skirmish.'
 */
public class Card31_050 extends AbstractSite {
    public Card31_050() {
        super("Forest River", SitesBlock.HOBBIT, 5, 7, Direction.RIGHT);
        addKeyword(Keyword.FOREST);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.ASSIGNMENT, self)
                && PlayConditions.canExert(self, game, Filters.owner(playerId), Race.ORC)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.owner(playerId), Race.ORC));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose Gandalf or an [ELVEN] ally", Filters.or(Filters.name("Gandalf"), Filters.and(Culture.ELVEN, CardType.ALLY))) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new CantBeAssignedToSkirmishModifier(self, card)));
                        }
                    });
            return Collections.singletonList(action);
        }

        return null;
    }
}
