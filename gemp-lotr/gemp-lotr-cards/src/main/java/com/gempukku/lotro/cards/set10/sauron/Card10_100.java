package com.gempukku.lotro.cards.set10.sauron;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndReturnCardsToHandEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Event • Shadow
 * Game Text: Exert a [SAURON] minion to return a Free Peoples condition to its owner's hand (or 2 if both are [SHIRE]
 * conditions).
 */
public class Card10_100 extends AbstractEvent {
    public Card10_100() {
        super(Side.SHADOW, 1, Culture.SAURON, "Speak No More to Me", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game, Culture.SAURON, CardType.MINION);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.SAURON, CardType.MINION));
        List<Effect> possibleEffects = new LinkedList<Effect>();
        possibleEffects.add(
                new ChooseAndReturnCardsToHandEffect(action, playerId, 1, 1, Side.FREE_PEOPLE, CardType.CONDITION) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Return Free Peoples condition to its owner's hand";
                    }
                });
        possibleEffects.add(
                new ChooseAndReturnCardsToHandEffect(action, playerId, 2, 2, Side.FREE_PEOPLE, CardType.CONDITION, Culture.SHIRE) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Return 2 Free Peoples SHIRE conditions to its owner's hand";
                    }
                });
        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
    }
}
