package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.ExertExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.actions.PlayPermanentAction;
import com.gempukku.lotro.logic.effects.CommonEffects;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 2
 * •Gates of the Hornburg
 * Possession • Support Area
 * Fortification.
 * To play, exert 2 [Rohan] companions.
 * Each minion skirmishing an unbound companion is strength -2.
 * Discard this condition at the start of the regroup phase.
 * http://lotrtcg.org/coreset/rohan/gatesofthehornburg(r1).png
 */
public class Card20_328 extends AbstractPermanent {
    public Card20_328() {
        super(Side.FREE_PEOPLE, 2, CardType.POSSESSION, Culture.ROHAN, "Gates of the Hornburg", null, true);
        addKeyword(Keyword.FORTIFICATION);
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ExertExtraPlayCostModifier(self, self, null, Culture.ROHAN, CardType.COMPANION));
    }

        @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Arrays.asList(
                new StrengthModifier(self, Filters.and(CardType.MINION, Filters.inSkirmishAgainst(Filters.unboundCompanion)), -2));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        return CommonEffects.getSelfDiscardAtStartOfRegroup(game, effectResult, self);
    }
}
