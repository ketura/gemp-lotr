package com.gempukku.lotro.cards.set12.wraith;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.ExtraFilters;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 2
 * Type: Event • Shadow
 * Game Text: Toil 2. (For each [WRAITH] character you exert when playing this, its twilight cost is -2) Spot your
 * Nazgul at a battleground or forest site to play a mount on him from your draw deck.
 */
public class Card12_164 extends AbstractEvent {
    public Card12_164() {
        super(Side.SHADOW, 2, Culture.WRAITH, "Echo of Hooves", Phase.SHADOW);
        addKeyword(Keyword.TOIL, 2);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Filters.owner(playerId), Race.NAZGUL)
                && PlayConditions.location(game, Filters.or(Keyword.BATTLEGROUND, Keyword.FOREST));
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndPlayCardFromDeckEffect(playerId, PossessionClass.MOUNT, ExtraFilters.attachableTo(game, Filters.owner(playerId), Race.NAZGUL)) {
                    @Override
                    protected void cardSelectedToPlay(LotroGame game, PhysicalCard selectedCard) {
                        game.getActionsEnvironment().addActionToStack(((AbstractAttachable) selectedCard.getBlueprint()).getPlayCardAction(playerId, game, selectedCard, Filters.and(Filters.owner(playerId), Race.NAZGUL), 0));
                    }
                });
        return action;
    }
}
