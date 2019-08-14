package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * 1
 * Legacy of Elbereth
 * Event • Skirmish
 * Spot 3 Elves to make a minion skirmishing an Elf strength -3.
 * http://lotrtcg.org/coreset/elven/legacyofelbereth(r1).png
 */
public class Card20_092 extends AbstractEvent {
    public Card20_092() {
        super(Side.FREE_PEOPLE , 1, Culture.ELVEN, "Legacy of Elbereth", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 3, Race.ELF);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, -3, CardType.MINION, Filters.inSkirmishAgainst(Race.ELF)));
        return action;
    }
}
