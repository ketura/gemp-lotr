package com.gempukku.lotro.cards.set32.dwarven;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractFollower;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.*;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Follower • Dwarf
 * Strength: 1
 * Game Text: Aid - Exert Gandalf and add (2). Each time bearer wins a skirmish, you may play a Dwarven
 * possession or Dwarven artifact from your draw deck on bearer.
 */
public class Card32_009 extends AbstractFollower {
    public Card32_009() {
        super(Side.FREE_PEOPLE, 2, 1, 0, 0, Culture.DWARVEN, "Thrain", "Father of Thorin", true);
    }

    @Override
    public Race getRace() {
        return Race.DWARF;
    }


    @Override
    public boolean canPayAidCost(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Filters.name("Gandalf"));
    }

    @Override
    public void appendAidCosts(LotroGame game, CostToEffectAction action, PhysicalCard self) {
        action.appendCost(new ChooseAndExertCharactersEffect(action, self.getOwner(), 1, 1, Filters.name("Gandalf")));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.transferredCard(game, effectResult, self, null, CardType.COMPANION)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddTwilightEffect(self, 2));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.hasAttached(self))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ChooseArbitraryCardsEffect(playerId, "Choose card to play", game.getGameState().getDeck(playerId),
                            Filters.and(Culture.DWARVEN, Filters.or(CardType.POSSESSION, CardType.ARTIFACT),
                                    ExtraFilters.attachableTo(game, Filters.hasAttached(self))), 1, 1) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            if (selectedCards.size() > 0) {
                                PhysicalCard selectedCard = selectedCards.iterator().next();
                                game.getActionsEnvironment().addActionToStack(PlayUtils.getPlayCardAction(game, selectedCard, 0, Filters.hasAttached(self), false));
                            }
                        }

                        @Override
                        public String getText(LotroGame game) {
                            return "Play DWARVEN possession or artifact from draw deck";
                        }
                    });
            if (PlayConditions.canPlayFromDiscard(playerId, game,
                    Filters.and(Culture.DWARVEN, Filters.or(CardType.POSSESSION, CardType.ARTIFACT),
                            ExtraFilters.attachableTo(game, Filters.hasAttached(self))))) {
                possibleEffects.add(
                        new ChooseArbitraryCardsEffect(playerId, "Choose card to play", game.getGameState().getDiscard(playerId),
                                Filters.and(Culture.DWARVEN, Filters.or(CardType.POSSESSION, CardType.ARTIFACT),
                                        ExtraFilters.attachableTo(game, Filters.hasAttached(self))), 1, 1) {
                            @Override
                            protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                                if (selectedCards.size() > 0) {
                                    PhysicalCard selectedCard = selectedCards.iterator().next();
                                    game.getActionsEnvironment().addActionToStack(PlayUtils.getPlayCardAction(game, selectedCard, 0, Filters.hasAttached(self), false));
                                }
                            }

                            @Override
                            public String getText(LotroGame game) {
                                return "Play DWARVEN possession or artifact from discard pile";
                            }
                        });
            }
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
