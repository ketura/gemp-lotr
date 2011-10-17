package com.gempukku.lotro.cards.set5.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 7
 * Vitality: 2
 * Site: 4
 * Game Text: Warg-rider. While Sharku is not exhausted, he is fierce. Each time you play an [ISENGARD] mount, you may
 * make the Free Peoples player exert a companion (except a Hobbit).
 */
public class Card5_058 extends AbstractMinion {
    public Card5_058() {
        super(2, 7, 2, 4, Race.ORC, Culture.ISENGARD, "Sharku", true);
        addKeyword(Keyword.WARG_RIDER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.and(Filters.sameCard(self), Filters.not(Filters.exhausted)), Keyword.FIERCE));
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game, effectResult, Culture.ISENGARD, PossessionClass.MOUNT)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, CardType.COMPANION, Filters.not(Race.HOBBIT)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
