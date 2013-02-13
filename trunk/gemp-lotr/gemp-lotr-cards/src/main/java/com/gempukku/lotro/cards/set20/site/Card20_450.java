package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Streets of Minas Tirith
 * 6	3
 * Sanctuary.
 * When the fellowship moves to streets of Minas Tirith, the Free Peoples player may exert a minion for each
 * [Gondor] companion he or she can spot.
 */
public class Card20_450 extends AbstractSite {
    public Card20_450() {
        super("Streets of Minas Tirith", Block.SECOND_ED, 6, 3, null);
        addKeyword(Keyword.SANCTUARY);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)
                && GameUtils.isFP(game, playerId)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            int count = Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Culture.GONDOR, CardType.COMPANION);
            for (int i=0; i<count; i++)
                action.appendEffect(
                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
