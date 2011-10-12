package com.gempukku.lotro.cards.set3.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 10
 * Vitality: 3
 * Site: 6
 * Game Text: Skirmish: Discard your [SAURON] condition to make a character skirmishing this minion strength -1.
 */
public class Card3_100 extends AbstractMinion {
    public Card3_100() {
        super(4, 10, 3, 6, Race.ORC, Culture.SAURON, "Orc Veteran");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self, 0)
                && Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.owner(playerId), Filters.culture(Culture.SAURON), Filters.type(CardType.CONDITION)) > 0) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.owner(playerId), Filters.culture(Culture.SAURON), Filters.type(CardType.CONDITION)));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose character skirmishing this minion", Filters.inSkirmishAgainst(Filters.sameCard(self))) {
                        @Override
                        protected void cardSelected(PhysicalCard card) {
                            action.insertEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.sameCard(card), -1), Phase.SKIRMISH));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
