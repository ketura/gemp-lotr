package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.costs.ChooseAndExertCharactersCost;
import com.gempukku.lotro.cards.effects.CardAffectsCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Shadow: Exert a [SAURON] Orc to discard a Free Peoples condition.
 */
public class Card1_277 extends AbstractEvent {
    public Card1_277() {
        super(Side.SHADOW, Culture.SAURON, "Shadow's Reach", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), Filters.race(Race.ORC), Filters.canExert());
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersCost(action, playerId, 1, 1, Filters.culture(Culture.SAURON), Filters.race(Race.ORC)));
        action.appendEffect(
                new ChooseActiveCardEffect(playerId, "Choose a Free Peoples condition", Filters.side(Side.FREE_PEOPLE), Filters.type(CardType.CONDITION)) {
                    @Override
                    protected void cardSelected(PhysicalCard fpCondition) {
                        action.appendEffect(new CardAffectsCardEffect(self, fpCondition));
                        action.appendEffect(
                                new DiscardCardsFromPlayEffect(self, fpCondition));
                    }
                });
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }
}
