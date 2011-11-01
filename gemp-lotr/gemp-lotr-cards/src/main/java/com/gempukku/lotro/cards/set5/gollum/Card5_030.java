package com.gempukku.lotro.cards.set5.gollum;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.DiscardCardAtRandomFromHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Shadow: Play Gollum from your draw deck or your discard pile. Skirmish: Discard a card at random from hand
 * to make Gollum strength +3.
 */
public class Card5_030 extends AbstractEvent {
    public Card5_030() {
        super(Side.SHADOW, 0, Culture.GOLLUM, "We Must Have It", Phase.SHADOW, Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && (
                game.getGameState().getCurrentPhase() != Phase.SKIRMISH
                        || Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.not(Filters.sameCard(self))).size() >= 1);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        if (game.getGameState().getCurrentPhase() == Phase.SHADOW) {
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Filters.gollum) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Play Gollum from your draw deck";
                        }
                    });
            if (PlayConditions.canPlayFromDiscard(playerId, game, Filters.gollum)) {
                possibleEffects.add(
                        new ChooseAndPlayCardFromDiscardEffect(playerId, game.getGameState().getDiscard(playerId), Filters.gollum) {
                            @Override
                            public String getText(LotroGame game) {
                                return "Play Gollum from your discard pile";
                            }
                        });
            }
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));
        } else {
            action.appendCost(
                    new DiscardCardAtRandomFromHandEffect(self, playerId, false));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 3, Filters.gollum));
        }
        return action;
    }
}
