package com.gempukku.lotro.cards.set2.sauron;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.AttachPermanentAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DrawCardOrPutIntoHandResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: To play, exert a [SAURON] Orc. Plays on the Ring-bearer. Each time the Free Peoples player draws a card
 * (or takes a card into hand) during the fellowship phase, add a burden.
 */
public class Card2_094 extends AbstractAttachable {
    public Card2_094() {
        super(Side.SHADOW, CardType.CONDITION, 0, Culture.SAURON, null, "Verily I Come");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, Filter additionalAttachmentFilter, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, additionalAttachmentFilter, twilightModifier)
                && PlayConditions.canExert(self, game, Culture.SAURON, Race.ORC);
    }

    @Override
    public AttachPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, Filter additionalAttachmentFilter, int twilightModifier) {
        AttachPermanentAction action = super.getPlayCardAction(playerId, game, self, additionalAttachmentFilter, twilightModifier);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.SAURON, Race.ORC));
        return action;
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Keyword.RING_BEARER;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.DRAW_CARD_OR_PUT_INTO_HAND
                && ((DrawCardOrPutIntoHandResult) effectResult).getPlayerId().equals(game.getGameState().getCurrentPlayerId())
                && game.getGameState().getCurrentPhase() == Phase.FELLOWSHIP) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddBurdenEffect(self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
