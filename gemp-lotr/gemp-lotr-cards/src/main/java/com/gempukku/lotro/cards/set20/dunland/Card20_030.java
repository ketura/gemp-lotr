package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * 0
 * War Club
 * Dunland	Possession • Hand Weapon
 * +2
 * Bearer must be a [Dunland] Man.
 * Each time bearer wins a skirmish, you may discard 2 Free Peoples possessions.
 */
public class Card20_030 extends AbstractAttachable {
    public Card20_030() {
        super(Side.SHADOW, CardType.POSSESSION, 0, Culture.DUNLAND, PossessionClass.HAND_WEAPON, "War Club");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.DUNLAND, Race.MAN);
    }

    @Override
    public int getStrength() {
        return 2;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.hasAttached(self))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 2, 2, Side.FREE_PEOPLE, CardType.POSSESSION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
