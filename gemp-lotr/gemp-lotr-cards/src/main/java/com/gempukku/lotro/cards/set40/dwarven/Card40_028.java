package com.gempukku.lotro.cards.set40.dwarven;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.CheckPhaseLimitEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardStackedCardsEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndStackCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Seven for the Dwarf-lords
 * Set: Second Edition
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Artifact - Support Area
 * Card Number: 1R28
 * Game Text: Fellowship: Stack a [DWARVEN] card here from hand.
 * Skirmish: Discard 2 cards stacked here to play a [DWARVEN] skirmish event from your discard pile.
 * Regroup: Discard 3 cards stacked here and exert a Dwarf twice who is damage +X to draw X cards (limit once per phase).
 */
public class Card40_028 extends AbstractPermanent{
    public Card40_028() {
        super(Side.FREE_PEOPLE, 0, CardType.ARTIFACT, Culture.DWARVEN, Zone.SUPPORT, "Seven for the Dwarf-lords", null, true);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canStackCardFromHand(self, game, playerId, 1, self, Culture.DWARVEN)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndStackCardsFromHandEffect(action, playerId, 1, 1, self, Culture.DWARVEN));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canDiscardFromStacked(self, game, playerId, 2, self, Filters.any)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.DWARVEN, Keyword.SKIRMISH, CardType.EVENT)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardStackedCardsEffect(action, playerId, 2, 2, self, Filters.any));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.DWARVEN, Keyword.SKIRMISH, CardType.EVENT));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canDiscardFromStacked(self, game, playerId, 3, self, Filters.any)
                && PlayConditions.canExert(self, game, 2, Race.DWARF)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardStackedCardsEffect(action, playerId, 3, 3, self, Filters.any));
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Race.DWARF) {
                        @Override
                        protected void forEachCardExertedCallback(PhysicalCard character) {
                            int damageCount = game.getModifiersQuerying().getKeywordCount(game.getGameState(), character, Keyword.DAMAGE);
                            if (damageCount>0) {
                                action.appendEffect(
                                        new CheckPhaseLimitEffect(action, self, 1,
                                                new DrawCardsEffect(action, playerId, damageCount)));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
