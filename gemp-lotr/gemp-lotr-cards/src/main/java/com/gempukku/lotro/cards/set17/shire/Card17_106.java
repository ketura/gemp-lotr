package com.gempukku.lotro.cards.set17.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.CancelSkirmishEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Pipeweed. To play, spot a Hobbit. Skirmish: Discard a pipeweed from play to cancel a skirmish involving
 * an unbound Hobbit.
 */
public class Card17_106 extends AbstractPermanent {
    public Card17_106() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.SHIRE, "Halfling Leaf");
        addKeyword(Keyword.PIPEWEED);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Race.HOBBIT);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canDiscardFromPlay(self, game, Keyword.PIPEWEED)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Keyword.PIPEWEED));
            action.appendEffect(
                    new CancelSkirmishEffect(Filters.unboundCompanion, Race.HOBBIT));
            return Collections.singletonList(action);
        }
        return null;
    }
}
