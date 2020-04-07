package com.gempukku.lotro.cards.set6.gollum;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support arear Skirmish: Spot Gollum and discard 3 cards from hand to wound a companion
 * Gollum is skirmishing once (or twice if that companion is a [SHIRE] companion).
 */
public class Card6_046 extends AbstractPermanent {
    public Card6_046() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.GOLLUM, "They Stole It");
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSpot(game, Filters.gollum)
                && game.getGameState().getHand(playerId).size() >= 3) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 3));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose companion", CardType.COMPANION, Filters.inSkirmishAgainst(Filters.gollum)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new WoundCharactersEffect(self, card));
                            if (card.getBlueprint().getCulture() == Culture.SHIRE)
                                action.appendEffect(
                                        new WoundCharactersEffect(self, card));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
