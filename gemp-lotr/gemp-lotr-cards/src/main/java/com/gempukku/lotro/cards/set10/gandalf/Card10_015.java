package com.gempukku.lotro.cards.set10.gandalf;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Each time you lose initiative (except during the fellowship phase), you may spot a [GANDALF] companion
 * to choose an opponent who must discard one of his or her conditions.
 */
public class Card10_015 extends AbstractPermanent {
    public Card10_015() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GANDALF, Zone.SUPPORT, "Brooding on Tomorrow");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.losesInitiative(effectResult, Side.FREE_PEOPLE)
                && game.getGameState().getCurrentPhase() != Phase.FELLOWSHIP
                && PlayConditions.canSpot(game, Culture.GANDALF, CardType.COMPANION)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseOpponentEffect(playerId) {
                        @Override
                        protected void opponentChosen(String opponentId) {
                            action.appendEffect(
                                    new ChooseAndDiscardCardsFromPlayEffect(action, opponentId, 1, 1, Filters.owner(opponentId), CardType.CONDITION));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
