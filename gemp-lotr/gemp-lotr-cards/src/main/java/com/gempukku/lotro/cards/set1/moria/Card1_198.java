package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.DiscardCardAtRandomFromHandEffect;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.ExertExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Search. To play, exert a [MORIA] minion. Plays to your support area. Each time the fellowship moves to
 * site 4, 5, or 6 and contains a Dwarf or Elf, the Free Peoples player discards 2 cards at random from hand.
 */
public class Card1_198 extends AbstractPermanent {
    public Card1_198() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.MORIA, "Through the Misty Mountains");
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlay(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ExertExtraPlayCostModifier(self, self, null, Culture.MORIA, CardType.MINION));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, Filters.siteBlock(SitesBlock.FELLOWSHIP), Filters.or(
                Filters.siteNumber(4), Filters.siteNumber(5), Filters.siteNumber(6)))
                && Filters.canSpot(game, CardType.COMPANION, Filters.or(Race.ELF, Race.DWARF))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(new DiscardCardAtRandomFromHandEffect(self, game.getGameState().getCurrentPlayerId(), true));
            action.appendEffect(new DiscardCardAtRandomFromHandEffect(self, game.getGameState().getCurrentPlayerId(), true));
            return Collections.singletonList(action);
        }
        return null;
    }
}
