package com.gempukku.lotro.cards.set31.troll;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.DiscardFromPlayExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.actions.PlayPermanentAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Troll
 * Twilight Cost: 5
 * Type: Minion • Troll
 * Strength: 11
 * Vitality: 4
 * Site: 2
 * Game Text: Fierce. To play, discard an Orc. The twilight cost of each Troll is -2. Each time the Free Peoples
 * player discards a condition, you may discard an ally.
 */
public class Card31_064 extends AbstractMinion {
    public Card31_064() {
        super(5, 11, 4, 2, Race.TROLL, Culture.GUNDABAD, "Bert", "Troll of Ettenmoors", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new DiscardFromPlayExtraPlayCostModifier(self, self, 1, null, Race.ORC));
    }

        @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Arrays.asList(
                new TwilightCostModifier(self, Race.TROLL, -2));
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachDiscardedFromPlay(game, effectResult, CardType.CONDITION)
                && ((DiscardCardsFromPlayResult) effectResult).getPerformingPlayer() != null
                && !((DiscardCardsFromPlayResult) effectResult).getPerformingPlayer().equals(self.getOwner())) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.ALLY));
            return Collections.singletonList(action);
        }
        return null;
    }
}
