package com.gempukku.lotro.cards.set5.rohan;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndRemoveTokensFromCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Fortification. Plays to your support area. Maneuver: Spot 2 [ROHAN] Men to remove 2 tokens from a machine
 * or to take a [ROHAN] possession into hand from your discard pile. Discard this condition.
 */
public class Card5_079 extends AbstractPermanent {
    public Card5_079() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.ROHAN, Zone.SUPPORT, "Armory", true);
        addKeyword(Keyword.FORTIFICATION);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.MANEUVER, self)
                && PlayConditions.canSpot(game, 2, Culture.ROHAN, Race.MAN)) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ChooseAndRemoveTokensFromCardEffect(self, playerId, null, 2, Keyword.MACHINE) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Remove 2 tokens from a machine";
                        }
                    });
            possibleEffects.add(
                    new ChooseAndPutCardFromDiscardIntoHandEffect(action, playerId, 1, 1, Culture.ROHAN, CardType.POSSESSION));
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
