package com.gempukku.lotro.cards.set17.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 7
 * Vitality: 2
 * Site: 4
 * Game Text: Each [ORC] mount is twilight cost -1. Skirmish: Remove (3) to make a mounted [ORC] Orc strength +2.
 */
public class Card17_085 extends AbstractMinion {
    public Card17_085() {
        super(3, 7, 2, 4, Race.ORC, Culture.ORC, "Orkish Traveler");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new TwilightCostModifier(self, Filters.and(Culture.ORC, PossessionClass.MOUNT), -1);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 3)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(3));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Culture.ORC, Race.ORC, Filters.mounted));
            return Collections.singletonList(action);
        }
        return null;
    }
}
