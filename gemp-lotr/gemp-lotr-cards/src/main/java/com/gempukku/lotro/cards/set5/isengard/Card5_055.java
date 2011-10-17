package com.gempukku.lotro.cards.set5.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.TakeControlOfASiteEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.SkirmishResult;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 5
 * Vitality: 3
 * Site: 4
 * Game Text: Warg-rider. While this minion is not exhausted, it is fierce. Each time an [ISENGARD] Orc wins a skirmish,
 * you may exert this minion to control a site.
 */
public class Card5_055 extends AbstractMinion {
    public Card5_055() {
        super(2, 5, 3, 4, Race.ORC, Culture.ISENGARD, "Isengard Scout Troop");
        addKeyword(Keyword.WARG_RIDER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.and(Filters.sameCard(self), Filters.not(Filters.exhausted)), Keyword.FIERCE));
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.winsSkirmish(game.getGameState(), game.getModifiersQuerying(), effectResult, Culture.ISENGARD, Race.ORC)) {
            SkirmishResult result = (SkirmishResult) effectResult;
            Collection<PhysicalCard> winners = Filters.filter(result.getWinners(), game.getGameState(), game.getModifiersQuerying(), Culture.ISENGARD, Race.ORC, Filters.canExert(self));
            List<OptionalTriggerAction> actions = new LinkedList<OptionalTriggerAction>();
            for (PhysicalCard winner : winners) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                action.appendCost(
                        new ExertCharactersEffect(self, winner));
                action.appendEffect(
                        new TakeControlOfASiteEffect(self, playerId));
                actions.add(action);
            }
            return actions;
        }
        return null;
    }
}
