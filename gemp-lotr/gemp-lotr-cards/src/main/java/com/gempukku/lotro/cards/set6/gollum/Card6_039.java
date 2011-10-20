package com.gempukku.lotro.cards.set6.gollum;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Skirmish: Spot Smeagol and discard 3 cards from hand to wound a minion Smeagol
 * is skirmishing once (or twice if that minion is a [WRAITH] minion).
 */
public class Card6_039 extends AbstractPermanent {
    public Card6_039() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GOLLUM, Zone.SUPPORT, "Don't Look at Them");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && PlayConditions.canSpot(game, Filters.name("Smeagol"))
                && game.getGameState().getHand(playerId).size() >= 3) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 3));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a minion", CardType.MINION, Filters.inSkirmishAgainst(Filters.name("Smeagol")), Filters.canTakeWound) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new WoundCharactersEffect(self, card));
                            if (card.getBlueprint().getCulture() == Culture.WRAITH)
                                action.insertEffect(
                                        new WoundCharactersEffect(self, card));
                        }
                    });
            return Collections.singletonList(action);
        }
    }
}
