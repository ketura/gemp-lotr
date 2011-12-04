package com.gempukku.lotro.cards.set4.dunland;

import com.gempukku.lotro.cards.AbstractResponseOldEvent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
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
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Response: If a [DUNLAND] Man wins a skirmish, make him fierce and strength +4 until the regroup phase.
 */
public class Card4_037 extends AbstractResponseOldEvent {
    public Card4_037() {
        super(Side.SHADOW, Culture.DUNLAND, "War Cry of Dunland");
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.and(Culture.DUNLAND, Race.MAN))
                && checkPlayRequirements(playerId, game, self, 0, 0, false, false)) {
            PhysicalCard winner = ((CharacterWonSkirmishResult) effectResult).getWinner();
            final PlayEventAction action = new PlayEventAction(self);
            action.setText("Make " + GameUtils.getCardLink(winner) + " strength +4 and Fierce");
            action.insertEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new StrengthModifier(self, Filters.sameCard(winner), 4), Phase.REGROUP));
            action.insertEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new KeywordModifier(self, Filters.sameCard(winner), Keyword.FIERCE), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
