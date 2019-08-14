package com.gempukku.lotro.cards.set2.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Maneuver: Exert an Uruk-hai to discard an armor possession, helm possession, or shield possession (or all
 * such Free Peoples possessions if you can spot 6 companions).
 */
public class Card2_039 extends AbstractEvent {
    public Card2_039() {
        super(Side.SHADOW, 2, Culture.ISENGARD, "Beyond the Height of Men", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Race.URUK_HAI);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.URUK_HAI));
        if (PlayConditions.canSpot(game, 6, CardType.COMPANION))
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self.getOwner(), self,
                            Filters.and(
                                    Side.FREE_PEOPLE,
                                    CardType.POSSESSION,
                                    Filters.or(
                                            PossessionClass.ARMOR,
                                            PossessionClass.HELM,
                                            PossessionClass.SHIELD
                                    ))));
        else
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(
                            action, playerId, 1, 1,
                            CardType.POSSESSION,
                            Filters.or(
                                    PossessionClass.ARMOR,
                                    PossessionClass.HELM,
                                    PossessionClass.SHIELD
                            )));
        return action;
    }
}
