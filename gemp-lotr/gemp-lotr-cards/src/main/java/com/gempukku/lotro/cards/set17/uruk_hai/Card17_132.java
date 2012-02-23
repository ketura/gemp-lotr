package com.gempukku.lotro.cards.set17.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.modifiers.evaluator.CountCulturesEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 4
 * Type: Minion • Uruk-Hai
 * Strength: 9
 * Vitality: 2
 * Site: 5
 * Game Text: Fierce. Hunter 1. (While skirmishing a non-hunter character, this character is strength +1.).
 * When you play this minion, you may spot another [URUK-HAI] minion to add a threat for each Free Peoples culture
 * you can spot.
 */
public class Card17_132 extends AbstractMinion {
    public Card17_132() {
        super(4, 9, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "White Hand Taskmaster");
        addKeyword(Keyword.FIERCE);
        addKeyword(Keyword.HUNTER, 1);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, Filters.not(self), CardType.MINION, Culture.URUK_HAI)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddThreatsEffect(playerId, self, new CountCulturesEvaluator(Side.FREE_PEOPLE)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
