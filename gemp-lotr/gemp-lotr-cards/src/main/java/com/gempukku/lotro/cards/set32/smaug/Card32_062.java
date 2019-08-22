package com.gempukku.lotro.cards.set32.smaug;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Shadow
 * Culture: Smaug
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Each time you play Smaug, you may play a Shadow condition from your draw deck. Shadow: Discard
 * 3 Orcs from play to play Smaug from your draw deck or discard pile. Its twilight cost is -6.
 */

public class Card32_062 extends AbstractPermanent {
    public Card32_062() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.GUNDABAD, "Twisted Gold of Dragon", null, true);
    }
    
    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Filters.and(Filters.owner(self.getOwner()), Filters.name("Smaug")))) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
                action.appendEffect(new ChooseAndPlayCardFromDeckEffect(playerId, Side.SHADOW, CardType.CONDITION));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canDiscardFromPlay(self, game, 3, Race.ORC)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleEffects = new LinkedList<Effect>();
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 3, 3, Race.ORC));
            possibleEffects.add(
                    new ChooseAndPlayCardFromDeckEffect(playerId, -6, Filters.name("Smaug")) {
                @Override
                public String getText(LotroGame game) {
                    return "Play Smaug from deck";
                }
            });
            if (PlayConditions.canPlayFromDiscard(playerId, game, -6, Filters.name("Smaug"))) {
                possibleEffects.add(
                        new ChooseAndPlayCardFromDiscardEffect(playerId, game, -6, Filters.name("Smaug")) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Play Smaug from discard";
                    }
                });
            }
            action.appendEffect(new ChoiceEffect(action, self.getOwner(), possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
