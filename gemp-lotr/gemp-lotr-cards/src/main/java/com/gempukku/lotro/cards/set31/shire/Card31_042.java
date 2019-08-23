package com.gempukku.lotro.cards.set31.shire;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractFollower;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPutCardFromDeckIntoHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * •Old Thrush [Shire]
 Follower • Bird
 Twilight Cost 1
 Strength bonus: +1
 'Aid - Exert Bilbo.
 Maneuver: Discard this follower to take into hand a Free Peoples card (except [Gandalf] or [Elven]) from your draw deck
 or discard pile.'
 */
public class Card31_042 extends AbstractFollower {
    public Card31_042() {
        super(Side.FREE_PEOPLE, 1, 1, 0, 0, Culture.SHIRE, "Old Thrush", null, true);
    }

    @Override
    public boolean canPayAidCost(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Filters.name("Bilbo"));
    }

    @Override
    public void appendAidCosts(LotroGame game, CostToEffectAction action, PhysicalCard self) {
        action.appendCost(new ChooseAndExertCharactersEffect(action, self.getOwner(), 1, 1, Filters.name("Bilbo")));
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canSpot(game, Filters.hasAttached(self))
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action= new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ChooseAndPutCardFromDeckIntoHandEffect(action, playerId, 1, 1, Side.FREE_PEOPLE,
                            Filters.not(Filters.or(Culture.GANDALF, Culture.ELVEN))));
            possibleEffects.add(
                    new ChooseAndPutCardFromDiscardIntoHandEffect(action, playerId, 1, 1, Side.FREE_PEOPLE,
                            Filters.not(Filters.or(Culture.GANDALF, Culture.ELVEN))));
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
