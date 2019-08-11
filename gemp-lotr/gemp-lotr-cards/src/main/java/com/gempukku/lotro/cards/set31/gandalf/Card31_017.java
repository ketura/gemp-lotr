package com.gempukku.lotro.cards.set31.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.effects.OptionalEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.logic.modifiers.MoveLimitModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * •Radagast, The Brown [Gandalf]
 * Ally • Home 5 • Wizard
 * Twilight Cost 4
 * Strength 8
 * Vitality 4
 * Wise.
 * Fellowship: Play Gandalf from your discard pile to make the move limit for this turn +1. Choose an opponent who may
 * draw 2 cards.
 */
public class Card31_017 extends AbstractAlly {
    public Card31_017() {
        super(4, SitesBlock.HOBBIT, 5, 8, 4, Race.WIZARD, Culture.GANDALF, "Radagast", "The Brown", true);
        addKeyword(Keyword.WISE);
    }

    @Override
    protected List<ActivateCardAction> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canPlayFromDiscard(playerId, game, Filters.name("Gandalf"))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Filters.name("Gandalf")));
            action.appendEffect(
                    new AddUntilEndOfTurnModifierEffect(
                            new MoveLimitModifier(self, 1)));

            action.appendEffect(
                    new ChooseOpponentEffect(self.getOwner()) {
                        @Override
                        protected void opponentChosen(String opponentId) {
                            action.appendEffect(
                                    new OptionalEffect(action, opponentId,
                                            new DrawCardsEffect(action, opponentId, 2)));
                        }
                    });

            return Collections.singletonList(action);
        }
        return null;
    }
}
