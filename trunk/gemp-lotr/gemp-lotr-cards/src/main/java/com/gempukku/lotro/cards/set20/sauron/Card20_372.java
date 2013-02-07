package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.CharacterWonSkirmishResult;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 3
 * Orc Villain
 * Sauron	Minion • Orc
 * 9	3	6
 * Each time this minion wins a skirmish, you may discard a possession or condition of the losing character's culture.
 */
public class Card20_372 extends AbstractMinion {
    public Card20_372() {
        super(3, 9, 3, 6, Race.ORC, Culture.SAURON, "Orc Villain");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self)) {
            Set<Culture> losingCultures = new HashSet<Culture>();
            for (PhysicalCard physicalCard : ((CharacterWonSkirmishResult) effectResult).getInvolving())
                losingCultures.add(physicalCard.getBlueprint().getCulture());

            Culture[] culturesArr = losingCultures.toArray(new Culture[0]);

            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.or(CardType.POSSESSION, CardType.CONDITION), Filters.or(culturesArr)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
