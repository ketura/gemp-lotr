package com.gempukku.lotro.cards.set32.esgaroth;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.ExertCharactersEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Free
 * Culture: Esgaroth
 * Twilight Cost: 1
 * Type: Possession • Ranged Weapon
 * Strength: +1
 * Game Text: Bearer must Bard. Archery: Exert Bard and discard this possession to wound a minion twice.
 * If that minion is Smaug, wound it again.
 */
public class Card32_016 extends AbstractAttachableFPPossession {
    public Card32_016() {
        super(1, 1, 0, Culture.ESGAROTH, PossessionClass.RANGED_WEAPON, "Black Arrow", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Bard");
    }

    @Override
    public List<ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.ARCHERY, self)
                && PlayConditions.canExert(self, game, self.getAttachedTo())) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(action, self, Filters.name("Bard")));
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION) {
                        @Override
                        public void woundedCardsCallback(Collection<PhysicalCard> cards) {
                            for (PhysicalCard card : cards) {
                                action.appendEffect(new WoundCharactersEffect(self, card));
                                if (card.getBlueprint().getTitle().equals("Smaug"))
                                    action.appendEffect(new WoundCharactersEffect(self, card));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
