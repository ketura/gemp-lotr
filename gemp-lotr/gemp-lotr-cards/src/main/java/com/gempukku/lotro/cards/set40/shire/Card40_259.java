package com.gempukku.lotro.cards.set40.shire;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.CancelSkirmishEffect;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: Nimble and Deft
 * Set: Second Edition
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Event - Skirmish
 * Card Number: 1C259
 * Game Text: Stealth. At sites 1-5, spot 2 Hobbit companions to cancel a skirmish involving a Hobbit or make a Hobbit strength +2 at any site.
 */
public class Card40_259 extends AbstractEvent {
    public Card40_259() {
        super(Side.FREE_PEOPLE, 1, Culture.SHIRE, "Nimble and Deft", Phase.SKIRMISH);
        addKeyword(Keyword.STEALTH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        List<Effect> possibleEffects = new ArrayList<Effect>(2);
        if (PlayConditions.canSpot(game, 2, Race.HOBBIT, CardType.COMPANION)
                && game.getGameState().getCurrentSiteNumber() <= 5)
            possibleEffects.add(
                    new CancelSkirmishEffect(Race.HOBBIT));
        possibleEffects.add(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Race.HOBBIT) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Make a Hobbit strength +2";
                    }
                });
        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
    }
}
