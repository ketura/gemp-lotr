package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.ExertCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If a Nazgul wins a skirmish, the Free Peoples player chooses to either exert the Ring-bearer or
 * add a burden.
 */
public class Card1_214 extends AbstractResponseEvent {
    public Card1_214() {
        super(Side.SHADOW, 0, Culture.WRAITH, "In the Ringwraith's Wake");
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Race.NAZGUL)
                && PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)) {
            PlayEventAction action = new PlayEventAction(self);
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ExertCharactersEffect(action, self, game.getGameState().getRingBearer(game.getGameState().getCurrentPlayerId())) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert the Ring-bearer";
                        }
                    });
            possibleEffects.add(
                    new AddBurdenEffect(game.getGameState().getCurrentPlayerId(), self, 1));
            action.appendEffect(
                    new ChoiceEffect(action, game.getGameState().getCurrentPlayerId(), possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
