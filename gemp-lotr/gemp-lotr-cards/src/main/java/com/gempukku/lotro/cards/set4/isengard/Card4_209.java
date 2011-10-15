package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ExhaustCharacterEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 3
 * Type: Event
 * Game Text: Archery: Discard 2 [ISENGARD] archers to exhaust an unbound companion.
 */
public class Card4_209 extends AbstractOldEvent {
    public Card4_209() {
        super(Side.SHADOW, Culture.ISENGARD, "Volley Fire", Phase.ARCHERY);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.ISENGARD), Filters.keyword(Keyword.ARCHER)) >= 2;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 2, 2, Filters.culture(Culture.ISENGARD), Filters.keyword(Keyword.ARCHER)));
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose unbound companion", Filters.unboundCompanion, Filters.canExert(self)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.insertEffect(
                                new ExhaustCharacterEffect(playerId, action, card));
                    }
                });
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 3;
    }
}
