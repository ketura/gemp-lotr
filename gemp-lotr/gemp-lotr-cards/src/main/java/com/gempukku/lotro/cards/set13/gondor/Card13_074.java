package com.gempukku.lotro.cards.set13.gondor;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.CardMatchesEvaluator;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Make a [GONDOR] companion strength +2 (or +4 if that companion has resistance 6 or more).
 */
public class Card13_074 extends AbstractEvent {
    public Card13_074() {
        super(Side.FREE_PEOPLE, 1, Culture.GONDOR, "Rally the Company", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, new CardMatchesEvaluator(2, 4, Filters.minResistance(6)), Culture.GONDOR, CardType.COMPANION));
        return action;
    }
}
