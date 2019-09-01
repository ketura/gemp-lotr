package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.ExertExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Tale. Exert a Dwarf to play this condition. Plays to your support area. Each time your opponent plays an
 * Orc, that player discards the top card of his or her draw deck.
 */
public class Card1_016 extends AbstractPermanent {
    public Card1_016() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.DWARVEN, "Greatest Kingdom of My People", null, true);
        addKeyword(Keyword.TALE);
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlay(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ExertExtraPlayCostModifier(self, self, null, Race.DWARF));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Filters.not(Filters.owner(self.getOwner())), Race.ORC)) {
            String playerId = ((PlayCardResult) effectResult).getPlayedCard().getOwner();
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(new DiscardTopCardFromDeckEffect(self, playerId, true));
            return Collections.singletonList(action);
        }
        return null;
    }
}
