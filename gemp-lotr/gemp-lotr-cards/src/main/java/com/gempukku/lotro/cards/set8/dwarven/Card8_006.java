package com.gempukku.lotro.cards.set8.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event • Skirmish
 * Game Text: Spot a Dwarf who is damage +X to make him strength +X.
 */
public class Card8_006 extends AbstractEvent {
    public Card8_006() {
        super(Side.FREE_PEOPLE, 0, Culture.DWARVEN, "Honed", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Race.DWARF, Keyword.DAMAGE);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                                return game.getModifiersQuerying().getKeywordCount(game, cardAffected, Keyword.DAMAGE);
                            }
                        }, Race.DWARF, Keyword.DAMAGE));
        return action;
    }
}
