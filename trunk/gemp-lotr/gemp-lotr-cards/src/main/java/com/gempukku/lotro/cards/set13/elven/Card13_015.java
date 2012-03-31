package com.gempukku.lotro.cards.set13.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 0
 * Type: Companion • Elf
 * Strength: 3
 * Vitality: 3
 * Resistance: 7
 * Game Text: To play, spot 3 Elves. Each time the fellowship moves during the regroup phase, you may reveal a card from
 * the top of your draw deck for each forest on the adventure path to heal an Elf for each [ELVEN] card revealed.
 */
public class Card13_015 extends AbstractCompanion {
    public Card13_015() {
        super(0, 3, 3, 7, Culture.ELVEN, Race.ELF, null, "Galadriel", "Sorceress of the Hidden Land", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, 3, Race.ELF);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, final LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.moves(game, effectResult) && PlayConditions.isPhase(game, Phase.REGROUP)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new RevealTopCardsOfDrawDeckEffect(self, playerId, Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.SITE, Zone.ADVENTURE_PATH, Keyword.FOREST)) {
                        @Override
                        protected void cardsRevealed(List<PhysicalCard> cards) {
                            for (PhysicalCard revealedElvenCard : Filters.filter(cards, game.getGameState(), game.getModifiersQuerying(), Culture.ELVEN)) {
                                action.appendEffect(
                                        new ChooseAndHealCharactersEffect(action, playerId, Race.ELF));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
