package com.gempukku.lotro.cards.set15.gollum;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 2
 * Type: Event • Regroup
 * Game Text: Discard a [GOLLUM] minion from play to wound each companion with strength 8 or more.
 */
public class Card15_053 extends AbstractEvent {
    public Card15_053() {
        super(Side.SHADOW, 2, Culture.GOLLUM, "Unseen Foe", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canDiscardFromPlay(self, game, Culture.GOLLUM, CardType.MINION);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Culture.GOLLUM, CardType.MINION));
        action.appendEffect(
                new WoundCharactersEffect(self, CardType.COMPANION, Filters.not(Filters.lessStrengthThan(8))));
        return action;
    }
}
