package com.gempukku.lotro.cards.set7.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: Fellowship: Play a [GONDOR] Man to place a [GONDOR] token here. Fellowship: Play a [GONDOR] companion.
 * That companion's twilight cost is -1 for each token here. Discard this condition.
 */
public class Card7_123 extends AbstractPermanent {
    public Card7_123() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.GONDOR, Zone.SUPPORT, "Support of the City");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)) {
            List<ActivateCardAction> actions = new LinkedList<ActivateCardAction>();
            if (PlayConditions.canPlayFromHand(playerId, game, Culture.GONDOR, Race.MAN)) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Add a GONDOR token here");
                action.appendCost(
                        new ChooseAndPlayCardFromHandEffect(playerId, game, Culture.GONDOR, Race.MAN));
                action.appendEffect(
                        new AddTokenEffect(self, self, Token.GONDOR, 1));
                actions.add(action);
            }
            int tokenCount = game.getGameState().getTokenCount(self, Token.GONDOR);
            if (PlayConditions.canPlayFromHand(playerId, game, -tokenCount, Culture.GONDOR, CardType.COMPANION)) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Play a GONDOR companion with modified twilight cost");
                action.appendEffect(
                        new ChooseAndPlayCardFromHandEffect(playerId, game, -tokenCount, Culture.GONDOR, CardType.COMPANION));
                action.appendEffect(
                        new DiscardCardsFromPlayEffect(self, self));
                actions.add(action);
            }
            return actions;
        }
        return null;
    }
}
