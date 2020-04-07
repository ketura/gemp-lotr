package com.gempukku.lotro.cards.set13.elven;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.modifiers.PlayersCantUseCardPhaseSpecialAbilitiesModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Assignment: Discard this from play and assign a minion to an [ELVEN] companion. Those characters cannot
 * use skirmish special abilities until the start of the regroup phase.
 */
public class Card13_013 extends AbstractPermanent {
    public Card13_013() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.ELVEN, "Crashing Cavalry");
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.ASSIGNMENT, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a minion", CardType.MINION, Filters.assignableToSkirmishAgainst(Side.FREE_PEOPLE, Filters.and(Culture.ELVEN, CardType.COMPANION))) {
                        @Override
                        protected void cardSelected(LotroGame game, final PhysicalCard minion) {
                            action.insertEffect(
                                    new ChooseActiveCardEffect(self, playerId, "Choose an ELVEN companion", Culture.ELVEN, CardType.COMPANION, Filters.assignableToSkirmishAgainst(Side.FREE_PEOPLE, minion)) {
                                        @Override
                                        protected void cardSelected(LotroGame game, final PhysicalCard companion) {
                                            action.insertEffect(
                                                    new AssignmentEffect(playerId, companion, minion));
                                            action.appendEffect(
                                                    new AddUntilStartOfPhaseModifierEffect(
                                                            new PlayersCantUseCardPhaseSpecialAbilitiesModifier(
                                                                    self, Phase.SKIRMISH, Filters.or(minion, companion)), Phase.REGROUP));
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
