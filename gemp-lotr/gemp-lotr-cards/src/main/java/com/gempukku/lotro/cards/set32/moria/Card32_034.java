package com.gempukku.lotro.cards.set32.moria;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;
import java.util.LinkedList;

/**
 * Set: The Clouds Burst
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 5
 * Type: Minion • Orc
 * Strength: 10
 * Vitality: 4
 * Site: 4
 * Game Text: When you play this minion, you may play a Moria card from your draw deck or discard pile.
  */
public class Card32_034 extends AbstractMinion {
    public Card32_034() {
        super(5, 10, 4, 4, Race.ORC, Culture.MORIA, "The Wolves Army", null, true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Culture.MORIA, Filters.not(CardType.EVENT, Keyword.RESPONSE)) {
                @Override
                public String getText(LotroGame game) {
                    return "Play a Moria card from deck";
                }
            });
            if (PlayConditions.canPlayFromDiscard(playerId, game, Culture.MORIA, Filters.not(CardType.EVENT, Keyword.RESPONSE))) {
                possibleEffects.add(
                        new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.MORIA, Filters.not(CardType.EVENT, Keyword.RESPONSE)) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Play a Moria from discard";
                    }
                });
            }
            action.appendEffect(new ChoiceEffect(action, self.getOwner(), possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
