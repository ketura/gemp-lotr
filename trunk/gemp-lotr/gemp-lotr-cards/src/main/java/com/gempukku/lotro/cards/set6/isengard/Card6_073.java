package com.gempukku.lotro.cards.set6.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.cards.effects.StackCardFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardStackedCardsEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Possession
 * Game Text: Plays to your support area. Each time a regroup action discards an [ISENGARD] Orc, you may stack that Orc
 * on this card. Response: If an [ISENGARD] Orc is about to take a wound, discard 2 cards stacked here to prevent
 * that wound.
 */
public class Card6_073 extends AbstractPermanent {
    public Card6_073() {
        super(Side.SHADOW, 1, CardType.POSSESSION, Culture.ISENGARD, Zone.SUPPORT, "Scaffolding");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (PlayConditions.isGettingDiscarded(effect, game, Culture.ISENGARD, Race.ORC)) {
            DiscardCardsFromPlayEffect discardEffect = (DiscardCardsFromPlayEffect) effect;
            if (game.getGameState().getCurrentPhase() == Phase.REGROUP
                    && discardEffect.getSource() != null) {
                List<OptionalTriggerAction> actions = new LinkedList<OptionalTriggerAction>();
                final Collection<PhysicalCard> discardedOrcs = Filters.filter(discardEffect.getAffectedCardsMinusPrevented(game), game.getGameState(), game.getModifiersQuerying(), Culture.ISENGARD, Race.ORC);
                for (final PhysicalCard discardedOrc : discardedOrcs) {
                    OptionalTriggerAction action = new OptionalTriggerAction(self);
                    action.setText("Stack " + GameUtils.getCardLink(discardedOrc));
                    action.appendEffect(
                            new StackCardFromPlayEffect(discardedOrc, self));
                    actions.add(action);
                }

                return actions;
            }
        }
        return null;
    }


    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (PlayConditions.isGettingWounded(effect, game, Culture.ISENGARD, Race.ORC)
                && game.getGameState().getStackedCards(self).size() >= 2) {
            final WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardStackedCardsEffect(action, playerId, 2, 2, self, Filters.any));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose ISENGARD Orc", Culture.ISENGARD, Race.ORC, Filters.in(woundEffect.getAffectedCardsMinusPrevented(game))) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new PreventCardEffect(woundEffect, card));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
