package com.gempukku.lotro.cards.set13.men;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Skirmish: Discard this condition from play to make a [MEN] minion damage +1 for each of the following that
 * is true: it is at a plains site; it is bearing a possession; it is skirmishing a companion who
 * has resistance 4 or less.
 */
public class Card13_087 extends AbstractPermanent {
    public Card13_087() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.MEN, Zone.SUPPORT, "Driven From the Plains");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfDiscard(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Chooe a MEN minion", Culture.MEN, CardType.MINION) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            int count = 0;
                            if (PlayConditions.location(game, Keyword.PLAINS))
                                count++;
                            if (PlayConditions.canSpot(game, card, Filters.hasAttached(CardType.POSSESSION)))
                                count++;
                            if (PlayConditions.canSpot(game, card, Filters.inSkirmishAgainst(CardType.COMPANION, Filters.maxResistance(4))))
                                count++;
                            if (count > 0)
                                action.appendEffect(
                                        new AddUntilEndOfPhaseModifierEffect(
                                                new KeywordModifier(self, card, Keyword.DAMAGE, count), Phase.SKIRMISH));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
