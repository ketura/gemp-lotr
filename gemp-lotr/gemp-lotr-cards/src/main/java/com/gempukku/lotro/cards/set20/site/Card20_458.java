package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.Collections;
import java.util.List;

/**
 * Cair Andros
 * 8	8
 * River.
 * Each time you play an Easterling using toil, the Free Peoples player must exert a companion.
 */
public class Card20_458 extends AbstractSite {
    public Card20_458() {
        super("Cair Andros", Block.SECOND_ED, 8, 8, null);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Keyword.EASTERLING)) {
            PlayCardResult playResult = (PlayCardResult) effectResult;
            if (playResult.isPaidToil()) {
                RequiredTriggerAction action = new RequiredTriggerAction(self);
                action.appendEffect(
                        new ChooseAndExertCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, CardType.COMPANION));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
