package com.gempukku.lotro.cards.set32.smaug;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.IncrementPhaseLimitEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Shadow
 * Culture: Smaug
 * Twilight Cost: 0
 * Type: Condition • Support area
 * Game Text: Each time Smaug wins a skirmish, you may add a doubt (or 2 doubts if a Dwarven companion he was
 * skirmishing is killed).
 */
public class Card32_059 extends AbstractPermanent {
    public Card32_059() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.GUNDABAD, "Dragon's Malice", null, true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.checkPhaseLimit(game, self, 1)
                && (TriggerConditions.forEachKilledBy(game, effectResult, Filters.name("Smaug"), Filters.and(CardType.COMPANION, Culture.DWARVEN))
                || TriggerConditions.losesSkirmishInvolving(game, effectResult, Filters.or(CardType.COMPANION, CardType.ALLY), Filters.name("Smaug")))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new IncrementPhaseLimitEffect(self, 1));
            if (TriggerConditions.forEachKilledBy(game, effectResult, Filters.name("Smaug"), Filters.and(CardType.COMPANION, Culture.DWARVEN))) {
                action.appendEffect(
                        new AddBurdenEffect(game.getGameState().getCurrentPlayerId(), self, 2));
            } else if (TriggerConditions.losesSkirmishInvolving(game, effectResult, Filters.or(CardType.COMPANION, CardType.ALLY), Filters.name("Smaug"))) {
                action.appendEffect(
                        new AddBurdenEffect(game.getGameState().getCurrentPlayerId(), self, 1));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
