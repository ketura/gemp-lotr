package com.gempukku.lotro.cards.set4.dunland;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 0
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be a [DUNLAND] Man. Each time bearer wins a skirmish, you may discard 2 Free Peoples
 * possessions.
 */
public class Card4_036 extends AbstractAttachable {
    public Card4_036() {
        super(Side.SHADOW, CardType.POSSESSION, 0, Culture.DUNLAND, Keyword.HAND_WEAPON, "War Club");
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.culture(Culture.DUNLAND), Filters.race(Race.MAN));
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.winsSkirmish(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.hasAttached(self))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 2, 2, Filters.type(CardType.POSSESSION), Filters.side(Side.FREE_PEOPLE)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
