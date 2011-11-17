package com.gempukku.lotro.cards.set5.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.Race;
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
 * Strength: 6
 * Vitality: 2
 * Site: 4
 * Game Text: Warg-rider. When you play this minion, you may play an [ISENGARD] mount from your discard pile. While
 * this minion is not exhausted, he is fierce.
 */
public class Card5_066 extends AbstractMinion {
    public Card5_066() {
        super(2, 6, 2, 4, Race.ORC, Culture.ISENGARD, "Warg-master");
        addKeyword(Keyword.WARG_RIDER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.and(Filters.sameCard(self), Filters.not(Filters.exhausted)), Keyword.FIERCE));
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.ISENGARD, PossessionClass.MOUNT)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.ISENGARD, PossessionClass.MOUNT));
            return Collections.singletonList(action);
        }
        return null;
    }
}
