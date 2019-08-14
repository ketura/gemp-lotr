package com.gempukku.lotro.cards.set11.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 6
 * Type: Event • Fellowship
 * Game Text: Spell. Toil 2. (For each [GANDALF] character you exert when playing this, its twilight cost is -2.)
 * Spot a [GANDALF] Wizard to discard 2 conditions from play.
 */
public class Card11_038 extends AbstractEvent {
    public Card11_038() {
        super(Side.FREE_PEOPLE, 6, Culture.GANDALF, "New-awakened", Phase.FELLOWSHIP);
        addKeyword(Keyword.SPELL);
        addKeyword(Keyword.TOIL, 2);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.GANDALF, Race.WIZARD);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 2, 2, CardType.CONDITION));
        return action;
    }
}
