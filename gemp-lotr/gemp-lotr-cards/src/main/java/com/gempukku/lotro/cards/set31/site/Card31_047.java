package com.gempukku.lotro.cards.set31.site;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.SpotEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.modifiers.CantDiscardFromPlayModifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Beorn's House. Site card.
 * Site Number: 4
 * Shadow Number: 4
 * 'Fellowship: Spot a [Gandalf] staff or play it from your draw deck to prevent Gandalf from being discarded until
 * the end of the turn.'
 */
public class Card31_047 extends AbstractSite {
    public Card31_047() {
        super("Beorn's House", SitesBlock.HOBBIT, 4, 4, Direction.RIGHT);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.FELLOWSHIP, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new SpotEffect(1, Culture.GANDALF, PossessionClass.STAFF));
            possibleCosts.add(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Culture.GANDALF, PossessionClass.STAFF));
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose Gandalf", Filters.name("Gandalf")) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new AddUntilEndOfTurnModifierEffect(
                                            new CantDiscardFromPlayModifier(self, "Can't be discarded", card, Filters.any)));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
