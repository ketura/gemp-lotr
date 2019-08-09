package com.gempukku.lotro.cards.set20.wraith;

import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.CharacterWonSkirmishResult;

import java.util.Collections;
import java.util.List;

/**
 * 0
 * Coming for the Ring
 * Event • Response
 * If a Nazgul wins a skirmish, exert the Ring-bearer (or add a burden if that minion was a twilight Nazgul).
 * http://lotrtcg.org/coreset/ringwraith/comingforthering(r1).png
 */
public class Card20_287 extends AbstractResponseEvent {
    public Card20_287() {
        super(Side.SHADOW, 0, Culture.WRAITH, "Coming for the Ring");
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, final LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Race.NAZGUL)) {
            CharacterWonSkirmishResult winResult = (CharacterWonSkirmishResult) effectResult;

            final PlayEventAction action = new PlayEventAction(self);
            action.setText("Play " + GameUtils.getFullName(self) + " for " + GameUtils.getFullName(winResult.getWinner()));
            if (Filters.and(Keyword.TWILIGHT).accepts(game, winResult.getWinner()))
                action.appendEffect(
                        new AddBurdenEffect(playerId, self, 1));
            else
                action.appendEffect(
                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.ringBearer));

            return Collections.singletonList(action);
        }
        return null;
    }
}
