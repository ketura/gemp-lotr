package com.gempukku.lotro.cards.set32.dwarven;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractFollower;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.modifiers.AllyParticipatesInArcheryFireAndSkirmishesModifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Follower • Bird
 * Strength: 1
 * Game Text: Aid - Exert Thorin. Maneuver: If Roäc is attached, discard him to play a Dwarven ally
 * from your draw deck, or to allow a Dwarven ally to participate in skirimishes until the regroup phase.
 */
public class Card32_008 extends AbstractFollower {
    public Card32_008() {
        super(Side.FREE_PEOPLE, 1, 1, 0, 0, Culture.DWARVEN, "Roac", "Son of Carc", true);
    }

    @Override
    public boolean canPayAidCost(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Filters.name("Thorin"));
    }

    @Override
    public void appendAidCosts(LotroGame game, CostToEffectAction action, PhysicalCard self) {
        action.appendCost(new ChooseAndExertCharactersEffect(action, self.getOwner(), 1, 1, Filters.name("Thorin")));
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canSpot(game, Filters.hasAttached(self))
                && PlayConditions.canSelfDiscard(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Culture.DWARVEN, CardType.ALLY));
            possibleEffects.add(
                    new ChooseActiveCardEffect(self, playerId, "Choose an Ally", Culture.DWARVEN, CardType.ALLY) {
                        @Override
                        public void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new AllyParticipatesInArcheryFireAndSkirmishesModifier(self, Filters.sameCard(card)), Phase.SKIRMISH) {

                                    });
                        }

                        @Override
                        public String getText(LotroGame game) {
                            return "Allow a DWARVEN ally to skirmish";
                        }
                    });
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
