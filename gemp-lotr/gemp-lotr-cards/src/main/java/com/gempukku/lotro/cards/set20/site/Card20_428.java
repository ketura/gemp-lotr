package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Banks of Nen Hithoel
 * 3	0
 * River. Sanctuary.
 * At the start of the fellowship phase, the Free Peoples player may heal a companion for each Free Peoples culture over 2.
 */
public class Card20_428 extends AbstractSite {
    public Card20_428() {
        super("Banks of Nen Hithoel", Block.SECOND_ED, 3, 0, null);
        addKeyword(Keyword.RIVER);
        addKeyword(Keyword.SANCTUARY);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.FELLOWSHIP)
                && GameUtils.isFP(game, playerId)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            int count = GameUtils.getSpottableFPCulturesCount(game, playerId)-2;
            for (int i=0; i<count; i++)
                action.appendEffect(
                        new ChooseAndHealCharactersEffect(action, playerId, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
