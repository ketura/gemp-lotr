package com.gempukku.lotro.cards.set5.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Machine. Plays to your support area. Shadow: Play an Uruk-hai to place an [ISENGARD] token on a machine.
 * Response: If one or more machines are about to be discarded by an opponent, discard this condition to prevent that.
 */
public class Card5_060 extends AbstractPermanent {
    public Card5_060() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.ISENGARD, Zone.SUPPORT, "Siege Engine");
        addKeyword(Keyword.MACHINE);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SHADOW, self, 0)
                && PlayConditions.canPlayFromHand(playerId, game, Race.URUK_HAI)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndPlayCardFromHandEffect(playerId, game.getGameState().getHand(playerId), Race.URUK_HAI));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a machine", Keyword.MACHINE) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new AddTokenEffect(self, card, Token.ISENGARD));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (PlayConditions.isGettingDiscarded(effect, game, Keyword.MACHINE)) {
            DiscardCardsFromPlayEffect discardEffect = (DiscardCardsFromPlayEffect) effect;
            if (discardEffect.getSource() != null
                    && discardEffect.getSource().getOwner().equals(game.getGameState().getCurrentPlayerId())) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.appendCost(
                        new DiscardCardsFromPlayEffect(self, self));

                Collection<PhysicalCard> machines = Filters.filter(discardEffect.getAffectedCardsMinusPrevented(game), game.getGameState(), game.getModifiersQuerying(), Keyword.MACHINE);
                for (PhysicalCard machine : machines)
                    action.appendEffect(
                            new PreventCardEffect(discardEffect, machine));

                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
