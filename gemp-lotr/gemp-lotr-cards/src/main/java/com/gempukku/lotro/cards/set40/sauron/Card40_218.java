package com.gempukku.lotro.cards.set40.sauron;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Title: *Gathering Evil
 * Set: Second Edition
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Condition - Support Area
 * Card Number: 1U218
 * Game Text: At the beginning of each Shadow phase, if you cannot spot more than 2 Free Peoples cultures,
 * you may play a [SAURON] minion from your draw deck or discard pile.
 */
public class Card40_218 extends AbstractPermanent {
    public Card40_218() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.SAURON, "Gathering Evil", null, true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.SHADOW)
                && !PlayConditions.canSpotFPCultures(game, 3, playerId)
                && (PlayConditions.canPlayFromDeck(playerId, game, Culture.SAURON, CardType.MINION)
                || PlayConditions.canPlayFromDiscard(playerId, game, Culture.SAURON, CardType.MINION))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            List<Effect> possibleEffects = new ArrayList<Effect>(2);
            possibleEffects.add(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Culture.SAURON, CardType.MINION));
            possibleEffects.add(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.SAURON, CardType.MINION));
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
