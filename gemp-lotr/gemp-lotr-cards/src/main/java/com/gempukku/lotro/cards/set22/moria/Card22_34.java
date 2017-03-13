package com.gempukku.lotro.cards.set22.moria;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.StackCardFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: To play, spot a [MORIA] card. Response: If your Orc wins a skirmish, discard cards and wounds on
 * that Orc and stack that Orc on this condition. Shadow: Play an Orc stacked here as if played from hand.
 */
public class Card1_183 extends AbstractPermanent {
    public Card1_183() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.MORIA, Zone.SUPPORT, "Goblin Swarms");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, 1, Culture.MORIA);
	}
	
    @Override
    public List<? extends Action> getExtraPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        List<PhysicalCard> stackedCards = game.getGameState().getStackedCards(self);
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && Filters.filter(stackedCards, game.getGameState(), game.getModifiersQuerying(), Filters.playable(game)).size() > 0) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseArbitraryCardsEffect(playerId, "Choose an Orc to play", stackedCards, Filters.playable(game), 1, 1) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> stackedOrcs) {
                            if (stackedOrcs.size() > 0) {
                                PhysicalCard stackedOrc = stackedOrcs.iterator().next();
                                game.getActionsEnvironment().addActionToStack(stackedOrc.getBlueprint().getPlayCardAction(playerId, game, stackedOrc, 0, false));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Race.ORC))
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Race.ORC, Filters.inSkirmish)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose an Orc", Race.ORC, Filters.inSkirmish) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard skirmishOrc) {
                            action.appendEffect(new StackCardFromPlayEffect(skirmishOrc, self));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}