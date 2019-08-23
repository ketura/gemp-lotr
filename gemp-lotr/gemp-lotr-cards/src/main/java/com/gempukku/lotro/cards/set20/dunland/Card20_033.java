package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.TakeControlOfASiteEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * Blood Oath
 * Dunland	Condition • Support Area
 * Regroup: If you do not control a site, discard a [Dunland] Man to take control of a site.
 * Skirmish: Discard this condition to make a [Dunland] man strength +2.
 */
public class Card20_033 extends AbstractPermanent {
    public Card20_033() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.DUNLAND, "Blood Oath");
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && !PlayConditions.canSpot(game, Filters.siteControlled(playerId))
                && PlayConditions.canDiscardFromPlay(self, game, Culture.DUNLAND, Race.MAN)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Culture.DUNLAND, Race.MAN));
            action.appendEffect(
                    new TakeControlOfASiteEffect(self, playerId));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
            && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Culture.DUNLAND, Race.MAN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
