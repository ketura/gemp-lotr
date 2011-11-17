package com.gempukku.lotro.cards.set2.isengard;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Maneuver: Exert an Uruk-hai to discard an armor possession, helm possession, or shield possession (or all
 * such Free Peoples possessions if you can spot 6 companions).
 */
public class Card2_039 extends AbstractOldEvent {
    public Card2_039() {
        super(Side.SHADOW, Culture.ISENGARD, "Beyond the Height of Men", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.URUK_HAI));
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.race(Race.URUK_HAI)));
        int companions = Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION);
        if (companions >= 6)
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self,
                            Filters.and(
                                    Filters.side(Side.FREE_PEOPLE),
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
