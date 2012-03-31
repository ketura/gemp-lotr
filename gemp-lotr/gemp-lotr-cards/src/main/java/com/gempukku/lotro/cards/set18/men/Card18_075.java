package com.gempukku.lotro.cards.set18.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.LiberateASiteEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.StackCardFromPlayEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 1
 * Type: Minion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Site: 2
 * Game Text: Maneuver: Liberate a site you control to make each [MEN] minion strength +2 until the regroup phase.
 * Regroup: Exert Ted Sandyman to stack a [MEN] Man in play on a possession.
 */
public class Card18_075 extends AbstractMinion {
    public Card18_075() {
        super(1, 3, 4, 2, Race.HOBBIT, Culture.MEN, "Ted Sandyman", "Chief's Men's Ally", true);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0)
                && PlayConditions.canLiberateASite(game, playerId)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new LiberateASiteEffect(self, playerId));
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new StrengthModifier(self, Filters.and(Culture.MEN, CardType.MINION), 2), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && PlayConditions.canSelfExert(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a MEN minion", Culture.MEN, CardType.MINION) {
                        @Override
                        protected void cardSelected(LotroGame game, final PhysicalCard minion) {
                            action.appendEffect(
                                    new ChooseActiveCardEffect(self, playerId, "Choose a possession", CardType.POSSESSION) {
                                        @Override
                                        protected void cardSelected(LotroGame game, PhysicalCard possession) {
                                            action.appendEffect(
                                                    new StackCardFromPlayEffect(minion, possession));
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
