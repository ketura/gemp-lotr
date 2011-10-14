package com.gempukku.lotro.cards.set4.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 6
 * Signet: Aragorn
 * Game Text: While you can spot a [ROHAN] Man, Eomer's twilight cost is -1. At the start of each turn, you may heal
 * a [ROHAN] ally.
 */
public class Card4_266 extends AbstractCompanion {
    public Card4_266() {
        super(3, 7, 3, Culture.ROHAN, Race.MAN, Signet.ARAGORN, "Eomer", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        twilightModifier -= (PlayConditions.canSpot(game, Filters.culture(Culture.ROHAN), Filters.race(Race.MAN))) ? 1 : 0;
        return super.checkPlayRequirements(playerId, game, self, twilightModifier);
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        twilightModifier -= (PlayConditions.canSpot(game, Filters.culture(Culture.ROHAN), Filters.race(Race.MAN))) ? 1 : 0;
        return super.getPlayCardAction(playerId, game, self, twilightModifier);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.START_OF_TURN) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, Filters.culture(Culture.ROHAN), Filters.type(CardType.ALLY)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
