package com.gempukku.lotro.cards.set7.gollum;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.CancelSkirmishEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
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
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        final PhysicalCard smeagol = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.smeagol);
        if (smeagol != null) {
            if (Filters.inSkirmish.accepts(game.getGameState(), game.getModifiersQuerying(), smeagol)
                    && hasMoreVitalityThanMinionsHeIsSkirmishing(game, smeagol)) {
                action.appendEffect(
                        new CancelSkirmishEffect(smeagol));
            } else {
                action.appendEffect(
                        new HealCharactersEffect(self, smeagol));
            }
        }
        return action;
    }

    private boolean hasMoreVitalityThanMinionsHeIsSkirmishing(LotroGame game, PhysicalCard smeagol) {
        int smeagolVitality = game.getModifiersQuerying().getVitality(game.getGameState(), smeagol);
        for (PhysicalCard minion : Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Filters.inSkirmishAgainst(smeagol))) {
            int minionVitality = game.getModifiersQuerying().getVitality(game.getGameState(), minion);
            if (minionVitality >= smeagolVitality)
                return false;
        }
        return true;
    }
}
