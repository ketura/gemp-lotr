package com.gempukku.lotro.cards.set20.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 5
 * •Ulaire Cantea, Morgul Predator
 * Ringwraith	Minion • Nazgul
 * 10	3	3
 * Fierce. At the start of each skirmish involving Ulaire Cantea, exert him and spot another Nazgul
 * to discard a possession borne by a companion skirmishing Ulaire Cantea.
 */
public class Card20_305 extends AbstractMinion {
    public Card20_305() {
        super(5, 10, 3, 3, Race.NAZGUL, Culture.WRAITH, Names.cantea, "Morgul Predator", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.SKIRMISH)
                && PlayConditions.isActive(game, self, Filters.inSkirmish)
                && PlayConditions.canSelfExert(self, game)
            && PlayConditions.canSpot(game, Filters.not(self), Race.NAZGUL)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, self.getOwner(), 1, 1, CardType.POSSESSION, Filters.attachedTo(CardType.COMPANION, Filters.inSkirmishAgainst(self))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
