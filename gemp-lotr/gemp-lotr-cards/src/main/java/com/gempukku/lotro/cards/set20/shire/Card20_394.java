package com.gempukku.lotro.cards.set20.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.CantAddBurdensModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.cost.ExertExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Legendary Adventures
 * Shire	Condition • Companion
 * 1
 * Tale.
 * To play exert Bilbo.
 * Bearer must be a Ring-bound Hobbit.
 * During the turn this condition was played, burdens may not be added by Shadow cards.
 */
public class Card20_394 extends AbstractAttachable {
    public Card20_394() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 1, Culture.SHIRE, null, "Legendary Adventures", null, true);
        addKeyword(Keyword.TALE);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Filters.name("Bilbo"));
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlay(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ExertExtraPlayCostModifier(self, self, null, 1, Filters.name("Bilbo")));
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Race.HOBBIT, Keyword.RING_BOUND);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(new CantAddBurdensModifier(self,
                new Condition() {
                    @Override
                    public boolean isFullfilled(LotroGame game) {
                        return game.getActionsEnvironment().getPlayedCardsInCurrentTurn().contains(self);
                    }
                }, Side.SHADOW));
    }
}
