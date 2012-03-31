package com.gempukku.lotro.cards.set18.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.modifiers.CancelStrengthBonusTargetModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 4
 * Type: Companion • Elf
 * Strength: 9
 * Vitality: 3
 * Resistance: 7
 * Game Text: Ranger. To play, spot 2 [ELVEN] companions. Minions skirmishing Glorfindel cannot gain strength bonuses
 * from possessions. Each time Glorfindel wins a skirmish, you may remove a threat.
 */
public class Card18_013 extends AbstractCompanion {
    public Card18_013() {
        super(4, 9, 3, 7, Culture.ELVEN, Race.ELF, null, "Glorfindel", "Eldarin Lord", true);
        addKeyword(Keyword.RANGER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, 2, Culture.ELVEN, CardType.COMPANION);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new CancelStrengthBonusTargetModifier(self, Filters.and(CardType.MINION, Filters.inSkirmishAgainst(self)), CardType.POSSESSION);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new RemoveThreatsEffect(self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
