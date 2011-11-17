package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Spell. Shadow: Exert a [ISENGARD] minion to discard all conditions.
 */
public class Card1_136 extends AbstractOldEvent {
    public Card1_136() {
        super(Side.SHADOW, Culture.ISENGARD, "Saruman's Power", Phase.SHADOW);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.ISENGARD), CardType.MINION);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.culture(Culture.ISENGARD), CardType.MINION));
        action.appendEffect(
                new DiscardCardsFromPlayEffect(self, CardType.CONDITION));
        return action;
    }
}
