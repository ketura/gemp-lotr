package com.gempukku.lotro.cards.set7.gollum;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.CancelSkirmishEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: Stealth. Heal Smeagol (or cancel Smeagol's skirmish if he has more vitality than the minion or minions
 * he is skirmishing).
 */
public class Card7_073 extends AbstractEvent {
    public Card7_073() {
        super(Side.FREE_PEOPLE, 2, Culture.GOLLUM, "Sneaking!", Phase.SKIRMISH);
        addKeyword(Keyword.STEALTH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        final PhysicalCard smeagol = Filters.findFirstActive(game, Filters.smeagol);
        if (smeagol != null) {
            if (Filters.inSkirmish.accepts(game, smeagol)
                    && hasMoreVitalityThanMinionsHeIsSkirmishing(game, smeagol)) {
                action.appendEffect(
                        new CancelSkirmishEffect(smeagol));
            } else {
                action.appendEffect(
                        new HealCharactersEffect(self, self.getOwner(), smeagol));
            }
        }
        return action;
    }

    private boolean hasMoreVitalityThanMinionsHeIsSkirmishing(LotroGame game, PhysicalCard smeagol) {
        int smeagolVitality = game.getModifiersQuerying().getVitality(game, smeagol);
        int minionVitality = 0;
        for (PhysicalCard minion : Filters.filterActive(game, Filters.inSkirmishAgainst(smeagol)))
            minionVitality += game.getModifiersQuerying().getVitality(game, minion);
        return smeagolVitality > minionVitality;
    }
}
