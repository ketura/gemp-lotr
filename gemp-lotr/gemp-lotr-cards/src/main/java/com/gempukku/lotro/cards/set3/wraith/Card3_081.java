package com.gempukku.lotro.cards.set3.wraith;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.AddBurdenResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: To play, spot a Nazgul. Plays to your support area. Each time a [WRAITH] card adds a burden,
 * the Free Peoples player must exert a companion.
 */
public class Card3_081 extends AbstractPermanent {
    public Card3_081() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.WRAITH, Zone.SHADOW_SUPPORT, "Gates of the Dead City");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.NAZGUL));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.ADD_BURDEN) {
            AddBurdenResult addBurdenResult = (AddBurdenResult) effectResult;
            if (addBurdenResult.getSource().getBlueprint().getCulture() == Culture.WRAITH) {
                RequiredTriggerAction action = new RequiredTriggerAction(self);
                action.appendEffect(
                        new ChooseAndExertCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, Filters.type(CardType.COMPANION)));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
