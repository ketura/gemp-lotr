package com.gempukku.lotro.cards.set20.wraith;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.CharacterWonSkirmishResult;

import java.util.Collections;
import java.util.List;

/**
 * 0
 * Coming for the Ring
 * Ringwraith	Event • Response
 * If a Nazgul wins a skirmish, wound the Ring-bearer (and add a burden if that minion was a twilight Nazgul).
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
            action.setText("Play "+ GameUtils.getFullName(self)+" for "+GameUtils.getFullName(winResult.getWinner()));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.ringBearer));
            if (Filters.and(Keyword.TWILIGHT).accepts(game.getGameState(), game.getModifiersQuerying(), winResult.getWinner()))
                action.appendEffect(
                        new AddBurdenEffect(playerId, self, 1));
            
            return Collections.singletonList(action);
        }
        return null;
    }
}
