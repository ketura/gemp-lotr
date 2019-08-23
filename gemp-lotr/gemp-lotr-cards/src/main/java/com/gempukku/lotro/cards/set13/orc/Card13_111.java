package com.gempukku.lotro.cards.set13.orc;

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
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;

import java.util.Collection;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 0
 * Type: Event • Skirmish
 * Game Text: Discard any number of your [ORC] minions from play to make an [ORC] minion strength +3 for each one
 * discarded.
 */
public class Card13_111 extends AbstractEvent {
    public Card13_111() {
        super(Side.SHADOW, 0, Culture.ORC, "Massing Strength", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 0, Integer.MAX_VALUE, Filters.owner(playerId), Culture.ORC, CardType.MINION) {
                    @Override
                    protected void forEachDiscardedByEffectCallback(Collection<PhysicalCard> cards) {
                        if (cards.size() > 0)
                            action.appendEffect(
                                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 3 * cards.size(), Culture.ORC, CardType.MINION));
                    }
                });
        return action;
    }
}
