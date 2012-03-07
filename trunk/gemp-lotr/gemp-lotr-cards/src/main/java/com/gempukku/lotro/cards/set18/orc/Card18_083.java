package com.gempukku.lotro.cards.set18.orc;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CardMatchesEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: Make a companion skirmishing an [ORC] minion strength -2 (or -3 if he or she is an archer).
 */
public class Card18_083 extends AbstractEvent {
    public Card18_083() {
        super(Side.SHADOW, 2, Culture.ORC, "Gruesome Meal", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new CardMatchesEvaluator(-2, -3, Keyword.ARCHER), CardType.COMPANION, Filters.inSkirmishAgainst(Culture.ORC, CardType.MINION)));
        return action;
    }
}
