package com.gempukku.lotro.cards.set18.gandalf;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.PlayersCantUseSpecialAbilitiesModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Maneuver: Add 2 threats and spot a Wizard to spot a minion in play. That minion cannot use special
 * abilities until the regroup phase.
 */
public class Card18_025 extends AbstractPermanent {
    public Card18_025() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.GANDALF, Zone.SUPPORT, "Perspective");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canAddThreat(game, self, 2)
                && PlayConditions.canSpot(game, Race.WIZARD)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a minion", CardType.MINION) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new PlayersCantUseSpecialAbilitiesModifier(self, card), Phase.REGROUP));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
