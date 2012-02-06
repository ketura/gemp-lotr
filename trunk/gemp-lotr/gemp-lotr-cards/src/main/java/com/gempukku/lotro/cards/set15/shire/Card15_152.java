package com.gempukku.lotro.cards.set15.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Response: If a Shadow card is about to add any number of burdens, discard a [SHIRE] follower
 * and this condition from play to prevent that and discard that Shadow card.
 */
public class Card15_152 extends AbstractPermanent {
    public Card15_152() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.SHIRE, Zone.SUPPORT, "Relaxation");
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, final Effect effect, PhysicalCard self) {
        if (TriggerConditions.isAddingBurden(effect, game, Side.SHADOW)
                && PlayConditions.canDiscardFromPlay(self, game, Culture.SHIRE, CardType.FOLLOWER)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Culture.SHIRE, CardType.FOLLOWER));
            action.appendCost(
                    new SelfDiscardEffect(self));
            final AddBurdenEffect addBurdenEffect = (AddBurdenEffect) effect;
            action.appendEffect(
                    new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            addBurdenEffect.preventAll();
                        }
                    });
            if (addBurdenEffect.getSource().getZone().isInPlay())
                action.appendEffect(
                        new DiscardCardsFromPlayEffect(self, addBurdenEffect.getSource()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
