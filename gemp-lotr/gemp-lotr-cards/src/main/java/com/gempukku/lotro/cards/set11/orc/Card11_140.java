package com.gempukku.lotro.cards.set11.orc;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CardMatchesEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Make an [ORC] minion strength +2 (or +4 if it is a lurker).
 */
public class Card11_140 extends AbstractEvent {
    public Card11_140() {
        super(Side.SHADOW, 1, Culture.ORC, "Strength in Shadows", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, new CardMatchesEvaluator(2, 4, Keyword.LURKER), Culture.ORC, CardType.MINION));
        return action;
    }
}
