package com.gempukku.lotro.cards.set5.raider;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: To play, spot a Southron. Plays to your support area. Maneuver: Discard this condition to add (1) for
 * each Man with ambush you spot.
 */
public class Card5_077 extends AbstractPermanent {
    public Card5_077() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.RAIDER, "Strength in Numbers");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Keyword.SOUTHRON);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0)
                && PlayConditions.canSelfDiscard(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ForEachYouSpotEffect(playerId, Keyword.AMBUSH) {
                        @Override
                        protected void spottedCards(int spotCount) {
                            action.insertEffect(
                                    new AddTwilightEffect(self, spotCount));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
