package com.gempukku.lotro.cards.set13.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.effects.KillEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Response: If Faramir is about to take a wound, discard this condition from play and place Boromir
 * or Denethor in the dead pile from play to prevent that and heal Faramir twice.
 */
public class Card13_060 extends AbstractPermanent {
    public Card13_060() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GONDOR, Zone.SUPPORT, "Away on the Wind");
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, final PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Filters.name("Faramir"))
                && PlayConditions.canSelfDiscard(self, game)
                && PlayConditions.canSpot(game, Filters.or(Filters.boromir, Filters.name("Denethor")))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendCost(
                    new ChooseActiveCardEffect(self, playerId, "Choose Boromir or Denethor", Filters.or(Filters.boromir, Filters.name("Denethor"))) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendCost(
                                    new KillEffect(card, KillEffect.Cause.CARD_EFFECT));
                        }
                    });
            PhysicalCard faramir = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name("Faramir"));
            if (faramir != null) {
                action.appendEffect(
                        new PreventCardEffect((WoundCharactersEffect) effect, faramir));
                action.appendEffect(
                        new HealCharactersEffect(self, faramir));
                action.appendEffect(
                        new HealCharactersEffect(self, faramir));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
