package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.results.CharacterWonSkirmishResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: To play, spot an Uruk-hai. Plays to your support area. Response: If your Uruk-hai wins a skirmish, remove
 * (3) to make him fierce until the regroup phase.
 */
public class Card1_159 extends AbstractPermanent {
    public Card1_159() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.ISENGARD, "Uruk-hai Rampage");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Race.URUK_HAI);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.and(Filters.owner(playerId), Race.URUK_HAI))
                && game.getGameState().getTwilightPool() >= 3) {
            CharacterWonSkirmishResult skirmishResult = ((CharacterWonSkirmishResult) effectResult);

            final ActivateCardAction action = new ActivateCardAction(self);
            action.setText("Make " + GameUtils.getFullName(skirmishResult.getWinner()) + " fierce");

            action.appendCost(new RemoveTwilightEffect(3));
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new KeywordModifier(self, skirmishResult.getWinner(), Keyword.FIERCE), Phase.REGROUP));

            return Collections.singletonList(action);
        }
        return null;
    }
}
