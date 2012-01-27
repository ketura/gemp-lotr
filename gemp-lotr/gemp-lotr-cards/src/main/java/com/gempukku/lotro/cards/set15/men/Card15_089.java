package com.gempukku.lotro.cards.set15.men;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.CantReplaceSiteByFPPlayerModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: While you can spot a [MEN] Man, the Free Peoples player cannot replace a site in the current region.
 * Archery: Spot a [MEN] Man and remove (3) to exert an unbound companion.
 */
public class Card15_089 extends AbstractPermanent {
    public Card15_089() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.MEN, Zone.SUPPORT, "Rapid Reload");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new CantReplaceSiteByFPPlayerModifier(self, new SpotCondition(Culture.MEN, Race.MAN), Filters.siteInCurrentRegion);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ARCHERY, self, 3)
                && PlayConditions.canSpot(game, Culture.MEN, Race.MAN)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(3));
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.unboundCompanion));
            return Collections.singletonList(action);
        }
        return null;
    }
}
