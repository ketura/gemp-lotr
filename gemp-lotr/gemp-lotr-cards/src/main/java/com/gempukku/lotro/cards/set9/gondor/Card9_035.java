package com.gempukku.lotro.cards.set9.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Reflections
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Artifact • Support Area
 * Game Text: To play, spot an unbound [GONDOR] Man. Response: If a [GONDOR] Man is about to take a wound, discard this
 * artifact to prevent that.
 */
public class Card9_035 extends AbstractPermanent {
    public Card9_035() {
        super(Side.FREE_PEOPLE, 1, CardType.ARTIFACT, Culture.GONDOR, Zone.SUPPORT, "Sapling of the White Tree");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, Culture.GONDOR, Race.MAN, Filters.unboundCompanion);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Culture.GONDOR, Race.MAN)
                && PlayConditions.canSelfDiscard(self, game)) {
            final WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a GONDOR Man", Culture.GONDOR, Race.MAN, Filters.in(woundEffect.getAffectedCardsMinusPrevented(game))) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new PreventCardEffect(woundEffect, card));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
