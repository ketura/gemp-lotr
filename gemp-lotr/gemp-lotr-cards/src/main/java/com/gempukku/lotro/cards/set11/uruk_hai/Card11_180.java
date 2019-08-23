package com.gempukku.lotro.cards.set11.uruk_hai;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.CountActiveEvaluator;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: Make an [URUK-HAI] minion strength +1 for each battleground site you can spot.
 */
public class Card11_180 extends AbstractEvent {
    public Card11_180() {
        super(Side.SHADOW, 2, Culture.URUK_HAI, "Brutality", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, new CountActiveEvaluator(CardType.SITE, Keyword.BATTLEGROUND), Culture.URUK_HAI, CardType.MINION));
        return action;
    }
}
