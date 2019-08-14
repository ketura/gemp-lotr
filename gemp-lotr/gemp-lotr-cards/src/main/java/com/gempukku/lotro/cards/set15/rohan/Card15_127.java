package com.gempukku.lotro.cards.set15.rohan;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Make a [ROHAN] Man strength +1 for each hunter you can spot (or if you can spot more valiant Men than
 * hunters, make a [ROHAN] Man strength +1 for each valiant Man you can spot).
 */
public class Card15_127 extends AbstractEvent {
    public Card15_127() {
        super(Side.FREE_PEOPLE, 1, Culture.ROHAN, "Grim Trophy", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(
                        action, self, playerId,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                                int hunters = Filters.countActive(game, Keyword.HUNTER);
                                int valiantMen = Filters.countActive(game, Race.MAN, Keyword.VALIANT);
                                return Math.max(hunters, valiantMen);
                            }
                        }, Culture.ROHAN, Race.MAN));
        return action;
    }
}
