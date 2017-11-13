package com.gempukku.lotro.cards.set31.gandalf;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.cards.modifiers.MoveLimitModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

public class Card31_017 extends AbstractAlly {
    public Card31_017() {
        super(4, Block.HOBBIT, 5, 8, 4, Race.WIZARD, Culture.GANDALF, "Radagast", "The Brown", true);
        addKeyword(Keyword.WISE);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
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
                                            new DrawCardsEffect(action, opponentId, 3)));
                        }
                    });

            return Collections.singletonList(action);
        }
        return null;
    }
}
