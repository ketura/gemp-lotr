package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.CancelEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Search. Response: If a stealth event is played, exert or discard your Uruk-hai to cancel that event.
 */
public class Card1_161 extends AbstractResponseEvent {
    public Card1_161() {
        super(Side.SHADOW, CardType.EVENT, Culture.ISENGARD, "Wariness");
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<? extends Action> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, EffectResult effectResult, final PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.and(Filters.type(CardType.EVENT), Filters.keyword(Keyword.STEALTH)))
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.URUK_HAI))) {

            final PlayEventAction action = new PlayEventAction(self);

            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new ChooseActiveCardEffect(playerId, "Choose an Uruk-hai to exert", Filters.keyword(Keyword.URUK_HAI), Filters.canExert()) {
                        @Override
                        public String getText() {
                            return "Exert an Uruk-hai";
                        }

                        @Override
                        protected void cardSelected(PhysicalCard urukHai) {
                            action.addCost(new ExertCharacterEffect(urukHai));
                        }
                    });
            possibleCosts.add(
                    new ChooseActiveCardEffect(playerId, "Choose an Uruk-hai to discard", Filters.keyword(Keyword.URUK_HAI)) {
                        @Override
                        public String getText() {
                            return "Discard an Uruk-hai";
                        }

                        @Override
                        protected void cardSelected(PhysicalCard urukHai) {
                            action.addCost(new DiscardCardFromPlayEffect(self, urukHai));
                        }
                    });

            action.addCost(
                    new ChoiceEffect(action, playerId, possibleCosts, true));

            action.addEffect(new CancelEffect(effect));

            return Collections.singletonList(action);
        }

        return null;
    }
}
