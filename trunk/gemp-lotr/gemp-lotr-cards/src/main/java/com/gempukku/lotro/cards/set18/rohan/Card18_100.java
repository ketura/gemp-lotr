package com.gempukku.lotro.cards.set18.rohan;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 0
 * Type: Possession
 * Game Text: Bearer must be a [ROHAN] Man or Merry. Skirmish: Discard a follower from play to make bearer strength +4.
 * If bearer is Gamling, you may also spot another companion to make him or her strength +4 until the regroup phase.
 */
public class Card18_100 extends AbstractAttachableFPPossession {
    public Card18_100() {
        super(0, 0, 0, Culture.ROHAN, null, "Gamling's Horn", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.or(Filters.name("Merry"), Filters.and(Culture.ROHAN, Race.MAN));
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canDiscardFromPlay(self, game, CardType.FOLLOWER)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.FOLLOWER));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, self.getAttachedTo(), 4), Phase.SKIRMISH));
            if (PlayConditions.canSpot(game, Filters.name("Gamling"), Filters.hasAttached(self))) {
                action.appendEffect(
                        new OptionalEffect(action, playerId,
                                new ChooseActiveCardEffect(self, playerId, "Choose another companion", Filters.not(self.getAttachedTo()), CardType.COMPANION) {
                                    @Override
                                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                                        action.appendEffect(
                                                new AddUntilStartOfPhaseModifierEffect(
                                                        new StrengthModifier(self, card, 4), Phase.REGROUP));
                                    }

                                    @Override
                                    public String getText(LotroGame game) {
                                        return "Spot another companion to make him or her strength +4 until the regroup phase";
                                    }
                                }));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
