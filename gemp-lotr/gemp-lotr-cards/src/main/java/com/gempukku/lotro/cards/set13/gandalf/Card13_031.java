package com.gempukku.lotro.cards.set13.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Spell. Spot 3 [GANDALF] companions to make a minion skirmishing a companion strength -3.
 */
public class Card13_031 extends AbstractEvent {
    public Card13_031() {
        super(Side.FREE_PEOPLE, 1, Culture.GANDALF, "The Flame of Anor", Phase.SKIRMISH);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 3, Culture.GANDALF, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, -3, CardType.MINION, Filters.inSkirmishAgainst(CardType.COMPANION)));
        return action;
    }
}
