package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractResponseOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.CancelEventEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.PlayEventResult;

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
public class Card1_161 extends AbstractResponseOldEvent {
    public Card1_161() {
        super(Side.SHADOW, Culture.ISENGARD, "Wariness");
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game, effectResult, CardType.EVENT, Filters.keyword(Keyword.STEALTH))
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Race.URUK_HAI)
                && checkPlayRequirements(playerId, game, self, 0, false)) {

            final PlayEventAction action = new PlayEventAction(self);

            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.URUK_HAI) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert an Uruk-hai";
                        }
                    });
            possibleCosts.add(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Race.URUK_HAI) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Discard an Uruk-hai";
                        }
                    });

            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));

            action.appendEffect(new CancelEventEffect(self, (PlayEventResult) effectResult));

            return Collections.singletonList(action);
        }

        return null;
    }
}
