package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.effects.PutOnTheOneRingEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Search. Maneuver: Exert an Uruk-hai to make the opponent choose to either exert 2 companions or make the
 * Ring-bearer put on The One Ring until the regroup phase.
 */
public class Card1_137 extends AbstractLotroCardBlueprint {
    public Card1_137() {
        super(Side.SHADOW, CardType.EVENT, Culture.ISENGARD, "Saruman's Reach");
        addKeyword(Keyword.SEARCH);
        addKeyword(Keyword.MANEUVER);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayShadowCardDuringPhase(game, Phase.MANEUVER, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.URUK_HAI), Filters.canExert())) {
            final PlayEventAction action = new PlayEventAction(self);
            action.addCost(
                    new ChooseActiveCardEffect(playerId, "Choose an Uruk-hai", Filters.keyword(Keyword.URUK_HAI), Filters.canExert()) {
                        @Override
                        protected void cardSelected(PhysicalCard urukHai) {
                            action.addCost(new ExertCharacterEffect(urukHai));
                        }
                    }
            );

            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ChooseActiveCardsEffect(playerId, "Choose characters to exert", 2, 2, Filters.type(CardType.COMPANION), Filters.canExert()) {
                        @Override
                        public String getText() {
                            return "Exert 2 companions";
                        }

                        @Override
                        protected void cardsSelected(List<PhysicalCard> cards) {
                            for (PhysicalCard card : cards)
                                action.addEffect(new ExertCharacterEffect(card));
                        }
                    });

            possibleEffects.add(new PutOnTheOneRingEffect());

            action.addEffect(
                    new ChoiceEffect(action, game.getGameState().getCurrentPlayerId(), possibleEffects, false));

            return Collections.singletonList(action);
        }

        return null;
    }
}
