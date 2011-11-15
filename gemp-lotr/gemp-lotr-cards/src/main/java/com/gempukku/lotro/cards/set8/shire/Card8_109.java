package com.gempukku.lotro.cards.set8.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.PutCardsFromHandBeneathDrawDeckEffect;
import com.gempukku.lotro.cards.effects.RevealCardsFromHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: Regroup: Exert an unbound Hobbit and add a burden to reveal your hand. Place each Free Peoples card
 * revealed this way beneath your draw deck.
 */
public class Card8_109 extends AbstractPermanent {
    public Card8_109() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.SHIRE, Zone.SUPPORT, "Closer and Closer He Bent");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canExert(self, game, Filters.unboundCompanion, Race.HOBBIT)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.unboundCompanion, Race.HOBBIT));
            action.appendCost(
                    new AddBurdenEffect(self, 1));
            action.appendEffect(
                    new RevealCardsFromHandEffect(self, playerId, new HashSet<PhysicalCard>(game.getGameState().getHand(playerId))));
            action.appendEffect(
                    new PutCardsFromHandBeneathDrawDeckEffect(action, playerId, Side.FREE_PEOPLE));
            return Collections.singletonList(action);
        }
        return null;
    }
}
