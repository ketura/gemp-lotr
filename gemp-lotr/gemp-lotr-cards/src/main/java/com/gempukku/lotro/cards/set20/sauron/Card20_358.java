package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 6
 * •Gothmog, Vile Commander
 * Sauron	Minion • Orc
 * 13	3	6
 * When you play this minion, you may make the Free Peoples player exert a companion for each Free Peoples culture
 * less than 4 that you can spot.
 */
public class Card20_358 extends AbstractMinion {
    public Card20_358() {
        super(6, 13, 3, 6, Race.ORC, Culture.SAURON, "Gothmog", "Vile Commander", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            int count = 4-GameUtils.getSpottableFPCulturesCount(game, playerId);
            for (int i=0; i<count; i++)
                action.appendEffect(
                        new ChooseAndExertCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
