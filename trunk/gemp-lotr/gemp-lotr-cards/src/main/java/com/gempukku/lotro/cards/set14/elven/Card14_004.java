package com.gempukku.lotro.cards.set14.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Expanded Middle-earth
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: At the start of each archery phase, if you can spot more minions than companions, each of your companions
 * is an archer.
 */
public class Card14_004 extends AbstractCompanion {
    public Card14_004() {
        super(2, 6, 3, 6, Culture.ELVEN, Race.ELF, null, "Gildor Inglorion", true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.ARCHERY)
                && Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.MINION)
                > Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new KeywordModifier(self, Filters.and(Filters.owner(self.getOwner()), CardType.COMPANION), Keyword.ARCHER), Phase.ARCHERY));
            return Collections.singletonList(action);
        }
        return null;
    }
}
