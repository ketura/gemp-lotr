package com.gempukku.lotro.cards.set12.uruk_hai;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 1
 * Type: Event • Shadow
 * Game Text: Spot an [URUK-HAI] minion to draw 3 cards. Then discard 2 cards from hand or discard an [URUK-HAI] minion
 * from hand.
 */
public class Card12_146 extends AbstractEvent {
    public Card12_146() {
        super(Side.SHADOW, 1, Culture.URUK_HAI, "Strange Device", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.URUK_HAI, CardType.MINION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new DrawCardsEffect(action, playerId, 3));
        List<Effect> possibleEffects = new LinkedList<Effect>();
        possibleEffects.add(
                new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 2) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Discard 2 cards from hand";
                    }
                });
        possibleEffects.add(
                new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Culture.URUK_HAI, CardType.MINION) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Discard an URUK-HAI minion";
                    }
                });
        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
    }
}
