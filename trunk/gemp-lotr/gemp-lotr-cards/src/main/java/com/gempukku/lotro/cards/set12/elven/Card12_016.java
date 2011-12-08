package com.gempukku.lotro.cards.set12.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.PutCardsFromDeckBeneathDrawDeckEffect;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.List;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: Spot an Elf to reveal cards from the top of your draw deck until you reveal a Shadow card. Make an Elf
 * strength +2 for each card revealed. Place the revealed cards in any order on the bottom of your draw deck.
 */
public class Card12_016 extends AbstractEvent {
    public Card12_016() {
        super(Side.FREE_PEOPLE, 2, Culture.ELVEN, "Attunement", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Race.ELF);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseActiveCardEffect(self, playerId, "Choose an Elf", Race.ELF) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        final int count = getCardsToRevealCount(playerId, game);
                        action.appendEffect(
                                new RevealTopCardsOfDrawDeckEffect(self, playerId, count) {
                                    @Override
                                    protected void cardsRevealed(List<PhysicalCard> cards) {
                                    }
                                });
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, card, count * 2), Phase.SKIRMISH));
                        action.appendEffect(
                                new PutCardsFromDeckBeneathDrawDeckEffect(action, self, playerId, game.getGameState().getDeck(playerId).subList(0, count)));
                    }
                });
        return action;
    }

    private int getCardsToRevealCount(String playerId, LotroGame game) {
        int count = 0;
        for (PhysicalCard physicalCard : game.getGameState().getDeck(playerId)) {
            count++;
            if (physicalCard.getBlueprint().getSide() == Side.SHADOW)
                break;
        }
        return count;
    }
}
