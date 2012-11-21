package com.gempukku.lotro.cards.set7.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.CharacterLostSkirmishResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 3
 * Site: 6
 * Game Text: Each time a companion is overwhelmed in a skirmish that involves a [SAURON] Orc, add 3 burdens.
 */
public class Card7_302 extends AbstractMinion {
    public Card7_302() {
        super(3, 9, 3, 6, Race.ORC, Culture.SAURON, "Orc Officer");
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.losesSkirmishInvolving(game, effectResult, CardType.COMPANION, Filters.and(Culture.SAURON, Race.ORC))) {
            CharacterLostSkirmishResult lostResult = (CharacterLostSkirmishResult) effectResult;
            if (lostResult.getSkirmishType() == CharacterLostSkirmishResult.SkirmishType.OVERWHELM) {
                RequiredTriggerAction action = new RequiredTriggerAction(self);
                action.appendEffect(
                        new AddBurdenEffect(self.getOwner(), self, 3));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
