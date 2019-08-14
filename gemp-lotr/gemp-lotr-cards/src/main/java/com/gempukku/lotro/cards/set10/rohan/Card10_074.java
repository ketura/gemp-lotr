package com.gempukku.lotro.cards.set10.rohan;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.LiberateASiteEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Event • Response
 * Game Text: If your [ROHAN] Man wins a skirmish, discard a [ROHAN] possession to wound a minion or to liberate a site.
 */
public class Card10_074 extends AbstractResponseEvent {
    public Card10_074() {
        super(Side.FREE_PEOPLE, 1, Culture.ROHAN, "Unyielding");
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Culture.ROHAN, Race.MAN, Filters.owner(playerId))
                && PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)
                && PlayConditions.canDiscardFromPlay(self, game, Culture.ROHAN, CardType.POSSESSION)) {
            PlayEventAction action = new PlayEventAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Culture.ROHAN, CardType.POSSESSION));
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Wound a minion";
                        }
                    });
            possibleEffects.add(
                    new LiberateASiteEffect(self, playerId, null));
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
