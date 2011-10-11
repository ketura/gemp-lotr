package com.gempukku.lotro.cards.set3.wraith;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. Each time the fellowship moves, add (1) for each burden you can spot. At
 * the end of each of your Shadow phases, exert a Nazgul or discard this condition.
 */
public class Card3_083 extends AbstractPermanent {
    public Card3_083() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.WRAITH, Zone.SUPPORT, "The Ring Draws Them", true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_FELLOWSHIP_MOVES) {
            int burdens = game.getGameState().getBurdens();
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddTwilightEffect(self, burdens));
            return Collections.singletonList(action);
        }
        if (effectResult.getType() == EffectResult.Type.END_OF_PHASE
                && game.getGameState().getCurrentPhase() == Phase.SHADOW) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ChooseAndExertCharactersEffect(action, self.getOwner(), 1, 1, Filters.race(Race.NAZGUL)));
            possibleEffects.add(
                    new DiscardCardsFromPlayEffect(self, self));
            action.appendEffect(
                    new ChoiceEffect(action, self.getOwner(), possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
