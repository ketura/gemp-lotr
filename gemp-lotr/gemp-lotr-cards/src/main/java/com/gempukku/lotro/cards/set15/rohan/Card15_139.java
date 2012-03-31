package com.gempukku.lotro.cards.set15.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.SpotEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Valiant. At the start of the maneuver phase, you may spot a hunter minion (or exert 2 valiant Men)
 * to make this companion defender +1 until the regroup phase.
 */
public class Card15_139 extends AbstractCompanion {
    public Card15_139() {
        super(3, 6, 3, 6, Culture.ROHAN, Race.MAN, null, "Rohirrim Warrior", null, true);
        addKeyword(Keyword.VALIANT);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.MANEUVER)
                && (PlayConditions.canSpot(game, CardType.MINION, Keyword.HUNTER)
                || PlayConditions.canExert(self, game, 1, 2, Race.MAN, Keyword.VALIANT))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new SpotEffect(1, CardType.MINION, Keyword.HUNTER) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Spot a hunter minion";
                        }
                    });
            possibleCosts.add(
                    new ChooseAndExertCharactersEffect(action, playerId, 2, 2, Race.MAN, Keyword.VALIANT) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert 2 valiant Men";
                        }
                    });
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new KeywordModifier(self, self, Keyword.DEFENDER, 1), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
