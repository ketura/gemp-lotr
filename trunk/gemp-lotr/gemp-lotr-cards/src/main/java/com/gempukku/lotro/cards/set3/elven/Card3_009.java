package com.gempukku.lotro.cards.set3.elven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.DiscardBottomCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
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
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Tale. Plays to your support area. Skirmish: Exert an [ELVEN] ally to discard the bottom card of your
 * draw deck. If that card is an [ELVEN] card, make a minion skirmishing an Elf strength -1.
 */
public class Card3_009 extends AbstractPermanent {
    public Card3_009() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.ELVEN, Zone.SUPPORT, "Beren and Luthien");
        addKeyword(Keyword.TALE);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Culture.ELVEN, CardType.ALLY)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.ELVEN, CardType.ALLY));
            action.appendEffect(
                    new DiscardBottomCardFromDeckEffect(playerId) {
                        @Override
                        protected void discardedCardCallback(PhysicalCard card) {
                            if (card.getBlueprint().getCulture() == Culture.ELVEN) {
                                action.appendEffect(
                                        new ChooseActiveCardEffect(self, playerId, "Choose a minion", CardType.MINION, Filters.inSkirmishAgainst(Filters.race(Race.ELF))) {
                                            @Override
                                            protected void cardSelected(LotroGame game, PhysicalCard minion) {
                                                action.insertEffect(
                                                        new AddUntilEndOfPhaseModifierEffect(
                                                                new StrengthModifier(self, Filters.sameCard(minion), -1), Phase.SKIRMISH));
                                            }
                                        });
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
