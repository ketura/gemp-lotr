package com.gempukku.lotro.cards.set3.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.ExertExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Tale. To play, exert a Dwarf. Plays to your support area. The twilight cost of each [MORIA] weapon is +2.
 * Fellowship: Discard this condition to discard up to 2 [MORIA] conditions.
 */
public class Card3_005 extends AbstractPermanent {
    public Card3_005() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.DWARVEN, "Song of Durin");
        addKeyword(Keyword.TALE);
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ExertExtraPlayCostModifier(self, self, null, Race.DWARF));
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Arrays.asList(
                new TwilightCostModifier(self, Filters.and(Culture.MORIA, Filters.weapon), 2));
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 0, 2, Culture.MORIA, CardType.CONDITION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
