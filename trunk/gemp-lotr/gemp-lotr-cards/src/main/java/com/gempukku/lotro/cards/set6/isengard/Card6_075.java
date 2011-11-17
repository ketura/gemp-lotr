package com.gempukku.lotro.cards.set6.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.StackCardFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. Each time a companion or ally loses a skirmish involving a [ISENGARD] Man,
 * you may spot a possession to stack that possession here.
 */
public class Card6_075 extends AbstractPermanent {
    public Card6_075() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.ISENGARD, Zone.SUPPORT, "Twisted Tales");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (PlayConditions.losesSkirmishAgainst(game, effectResult, Filters.or(CardType.COMPANION, CardType.ALLY), Filters.and(Culture.ISENGARD, Race.MAN))
                && PlayConditions.canSpot(game, CardType.POSSESSION)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose possession", CardType.POSSESSION) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new StackCardFromPlayEffect(card, self));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
