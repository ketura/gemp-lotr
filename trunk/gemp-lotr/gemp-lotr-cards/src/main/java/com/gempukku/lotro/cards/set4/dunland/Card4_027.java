package com.gempukku.lotro.cards.set4.dunland;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.CharacterWonSkirmishResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays to your support area. Each time a [DUNLAND] Man wins a skirmish involving a [ROHAN] Man, you may
 * make that minion strength +2 and fierce until the regroup phase.
 */
public class Card4_027 extends AbstractPermanent {
    public Card4_027() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.DUNLAND, Zone.SUPPORT, "Living Off Rock");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmishInvolving(game, effectResult, Filters.and(Culture.DUNLAND, Race.MAN), Filters.and(Culture.ROHAN, Race.MAN))) {
            CharacterWonSkirmishResult skirmishResult = (CharacterWonSkirmishResult) effectResult;
            PhysicalCard winner = skirmishResult.getWinner();
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.setText("Make " + GameUtils.getCardLink(winner) + " strength +2 and Fierce");
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new StrengthModifier(self, Filters.sameCard(winner), 2), Phase.REGROUP));
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new KeywordModifier(self, Filters.sameCard(winner), Keyword.FIERCE), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
