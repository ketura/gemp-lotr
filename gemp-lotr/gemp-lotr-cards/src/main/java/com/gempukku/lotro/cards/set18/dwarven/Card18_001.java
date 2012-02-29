package com.gempukku.lotro.cards.set18.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Companion • Dwarf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Damage +1. Hunter 1. (While skirmishing a non-hunter character, this character is strength +1.) Each time
 * Gimli wins a skirmish, you may add a threat to wound a minion (or wound a hunter minion twice).
 */
public class Card18_001 extends AbstractCompanion {
    public Card18_001() {
        super(2, 6, 3, 6, Culture.DWARVEN, Race.DWARF, null, "Gimli", true);
        addKeyword(Keyword.DAMAGE, 1);
        addKeyword(Keyword.HUNTER, 1);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self)
                && PlayConditions.canAddThreat(game, self, 1)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new AddThreatsEffect(playerId, self, 1));
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Wound a minion";
                        }
                    });
            possibleEffects.add(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION, Keyword.HUNTER) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Wound a hunter minion twice";
                        }
                    });
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
