package com.gempukku.lotro.cards.set13.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Archer. At the start of each archery phase, you may reveal the top card of your draw deck. If it is
 * an [ELVEN] card, wound a minion.
 */
public class Card13_018 extends AbstractCompanion {
    public Card13_018() {
        super(2, 6, 3, 6, Culture.ELVEN, Race.ELF, null, "Legolas", "Of the Grey Company", true);
        addKeyword(Keyword.ARCHER);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.ARCHERY)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new RevealTopCardsOfDrawDeckEffect(self, playerId, 1) {
                        @Override
                        protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                            for (PhysicalCard card : revealedCards) {
                                if (card.getBlueprint().getCulture() == Culture.ELVEN)
                                    action.appendEffect(
                                            new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
