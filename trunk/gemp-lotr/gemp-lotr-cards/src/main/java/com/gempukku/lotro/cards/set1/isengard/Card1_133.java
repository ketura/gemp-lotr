package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays to your support area. The twilight cost of your [ISENGARD] events is -1. Skirmish: Discard this
 * condition to make an Uruk-hai strength +2.
 */
public class Card1_133 extends AbstractPermanent {
    public Card1_133() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.ISENGARD, Zone.SUPPORT, "Saruman's Ambition");
    }

    @Override
    public List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self, 0)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Race.URUK_HAI)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new DiscardCardsFromPlayEffect(self, self));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Race.URUK_HAI));

            return Collections.singletonList(action);
        }

        return null;
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new TwilightCostModifier(self, Filters.and(Culture.ISENGARD, CardType.EVENT, Filters.owner(self.getOwner())), -1);
    }
}
