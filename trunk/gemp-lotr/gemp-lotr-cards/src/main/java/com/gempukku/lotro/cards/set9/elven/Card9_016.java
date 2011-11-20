package com.gempukku.lotro.cards.set9.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.Collections;
import java.util.List;

/**
 * Set: Reflections
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 4
 * Type: Companion • Elf
 * Strength: 9
 * Vitality: 3
 * Resistance: 6
 * Game Text: When Glorfindel is in your starting fellowship, his twilight cost is -2. Skirmish: Reveal the top card of
 * your draw deck. You may exert Glorfindel to make a Nazgul he is skirmishing strength -X, where X is the twilight cost
 * of the card revealed.
 */
public class Card9_016 extends AbstractCompanion {
    public Card9_016() {
        super(4, 9, 3, 6, Culture.ELVEN, Race.ELF, null, "Glorfindel", true);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        if (gameState.getCurrentPhase() == Phase.PLAY_STARTING_FELLOWSHIP)
            return -2;
        return 0;
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new RevealTopCardsOfDrawDeckEffect(self, playerId, 1) {
                        @Override
                        protected void cardsRevealed(List<PhysicalCard> cards) {
                            for (final PhysicalCard card : cards) {
                                if (PlayConditions.canSelfExert(self, game)) {
                                    action.appendEffect(
                                            new OptionalEffect(action, playerId,
                                                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, self) {
                                                        @Override
                                                        protected void forEachCardExertedCallback(PhysicalCard character) {
                                                            action.appendEffect(
                                                                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, -card.getBlueprint().getTwilightCost(), Race.NAZGUL, Filters.inSkirmishAgainst(self)));
                                                        }
                                                    }));
                                }
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
