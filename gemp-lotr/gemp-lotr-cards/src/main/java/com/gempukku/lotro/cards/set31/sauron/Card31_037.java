package com.gempukku.lotro.cards.set31.sauron;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.OptionalEffect;
import com.gempukku.lotro.logic.effects.SpotEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * •Bolg, Servant of Sauron [Sauron]
 * Minion • Orc
 * Twilight Cost 4
 * Strength 9
 * Vitality 3
 * Site 5
 * 'Damage +1.
 * When you play Bolg, you may play an Orc from your discard pile for each [Dwarven] companion over 3.
 * Maneuver: Spot an [Elven] archer to make Bolg fierce until the regroup phase.'
 */
public class Card31_037 extends AbstractMinion {
    public Card31_037() {
        super(4, 9, 3, 5, Race.ORC, Culture.SAURON, "Bolg", "Servant of Sauron", true);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, 4, Culture.DWARVEN, CardType.COMPANION)) {
            int playCount = Filters.countSpottable(game, Culture.DWARVEN, CardType.COMPANION) - 3;

            RequiredTriggerAction action = new RequiredTriggerAction(self);
            for (int i = 0; i < playCount; i++) {
                action.appendEffect(
                        new OptionalEffect(action, self.getOwner(),
                                new ChooseAndPlayCardFromDiscardEffect(self.getOwner(), game, Race.ORC)));
            }
            return Collections.singletonList(action);
        }

        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0)
                && PlayConditions.canSpot(game, Culture.ELVEN, Keyword.ARCHER)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SpotEffect(1, Culture.ELVEN, Keyword.ARCHER));
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new KeywordModifier(self, self, Keyword.FIERCE), Phase.REGROUP));

            return Collections.singletonList(action);
        }
        return null;
    }
}
