package com.gempukku.lotro.cards.set12.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.timing.ExtraFilters;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.owner(self.getOwner()), Race.NAZGUL)
                && PlayConditions.location(game, Filters.or(Keyword.BATTLEGROUND, Keyword.FOREST));
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndPlayCardFromDeckEffect(playerId, PossessionClass.MOUNT, ExtraFilters.attachableTo(game, Filters.owner(playerId), Race.NAZGUL)) {
                    @Override
                    protected void cardSelectedToPlay(LotroGame game, PhysicalCard selectedCard) {
                        game.getActionsEnvironment().addActionToStack(PlayUtils.getPlayCardAction(game, selectedCard, 0, Filters.and(Filters.owner(playerId), Race.NAZGUL), false));
                    }
                });
        return action;
    }
}
