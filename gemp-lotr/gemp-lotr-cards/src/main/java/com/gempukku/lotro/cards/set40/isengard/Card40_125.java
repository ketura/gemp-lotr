package com.gempukku.lotro.cards.set40.isengard;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.ExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Lurtz, Champion of the White Hand
 * Set: Second Edition
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 7
 * Type: Minion - Uruk-hai
 * Strength: 13
 * Vitality: 3
 * Home: 5
 * Card Number: 1R125
 * Game Text: Damage +1. The current site is a battleground.
 * Each time Lurtz wins a skirmish, the Free Peoples player must exert a companion. If that companion is a Hobbit,
 * exert it again.
 */
public class Card40_125 extends AbstractMinion {
    public Card40_125() {
        super(7, 13, 3, 5, Race.URUK_HAI, Culture.ISENGARD, "Lurtz", "Champion of the White Hand", true);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        KeywordModifier modifier = new KeywordModifier(self, Filters.currentSite, Keyword.BATTLEGROUND);
        return Collections.singletonList(modifier);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(final LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self)) {
            final RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, CardType.COMPANION) {
                        @Override
                        protected void forEachCardExertedCallback(PhysicalCard character) {
                            if (PlayConditions.canSpot(game, character, Race.HOBBIT)) {
                                action.appendEffect(
                                        new ExertCharactersEffect(action, self, character));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
