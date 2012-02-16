package com.gempukku.lotro.cards.set17.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 7
 * Type: Minion • Man
 * Strength: 15
 * Vitality: 4
 * Site: 4
 * Game Text: When you play this minion from your draw deck or from your [MEN] possesion it is fierce until
 * the regroup phase.
 */
public class Card17_064 extends AbstractMinion {
    public Card17_064() {
        super(7, 15, 4, 4, Race.MAN, Culture.MEN, "Vengeful Pillager");
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            PlayCardResult playResult = (PlayCardResult) effectResult;
            if (playResult.getPlayedFrom() == Zone.DECK ||
                    (playResult.getPlayedFrom() == Zone.STACKED
                            && Filters.and(Culture.MEN, CardType.POSSESSION).accepts(game.getGameState(), game.getModifiersQuerying(), playResult.getAttachedOrStackedPlayedFrom()))) {
                RequiredTriggerAction action = new RequiredTriggerAction(self);
                action.appendEffect(
                        new AddUntilStartOfPhaseModifierEffect(
                                new KeywordModifier(self, self, Keyword.FIERCE), Phase.REGROUP));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
