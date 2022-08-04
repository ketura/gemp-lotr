package com.gempukku.lotro.cards.set13.gollum;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Event • Fellowship
 * Game Text: Spot Smeagol to choose a Shadow player who must discard a Shadow card from hand or remove (2).
 */
public class Card13_047 extends AbstractEvent {
    public Card13_047() {
        super(Side.FREE_PEOPLE, 0, Culture.GOLLUM, "Duality", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.smeagol);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseOpponentEffect(playerId) {
                    @Override
                    protected void opponentChosen(String opponentId) {
                        List<Effect> possibleEffects = new LinkedList<>();
                        possibleEffects.add(
                                new ChooseAndDiscardCardsFromHandEffect(action, opponentId, false, 1, Side.SHADOW) {
                                    @Override
                                    public String getText(LotroGame game) {
                                        return "Discard Shadow card from hand";
                                    }
                                });
                        possibleEffects.add(
                                new RemoveTwilightEffect(2));
                        action.appendEffect(
                                new ChoiceEffect(action, opponentId, possibleEffects));
                    }
                });
        return action;
    }
}
