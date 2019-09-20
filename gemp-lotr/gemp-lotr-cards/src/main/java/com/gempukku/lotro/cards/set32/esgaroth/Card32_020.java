package com.gempukku.lotro.cards.set32.esgaroth;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.effects.DiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Free
 * Culture: Esgaroth
 * Twilight Cost: 2
 * Type: Ally • Home 6 • Man
 * Strength: 5
 * Vitality: 3
 * Site: 6
 * Game Text: At the start of each of your turns, you may spot Bard to heal Bard and The Master.
 * Fellowship: Exert The Master twice to discard up to 3 cards from hand and draw an equal number of cards.
 */
public class Card32_020 extends AbstractAlly {
    public Card32_020() {
        super(2, SitesBlock.HOBBIT, 6, 5, 3, Race.MAN, Culture.ESGAROTH, "The Master", "Master of Lake-Town", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.startOfTurn(game, effectResult)
                && PlayConditions.canSpot(game, Filters.name("Bard"))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new HealCharactersEffect(self, Filters.name("Bard")));
            action.appendEffect(
                    new HealCharactersEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<ActivateCardAction> getPhaseActionsInPlay(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game, 2, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseCardsFromHandEffect(playerId, 0, 3, Filters.any) {
                        @Override
                        public void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            action.appendEffect(new DiscardCardsFromHandEffect(self, playerId, selectedCards, false));
                            action.appendEffect(new DrawCardsEffect(action, playerId, selectedCards.size()));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
