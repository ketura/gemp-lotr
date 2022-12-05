package com.gempukku.lotro.cards.set3.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardBottomCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collection;
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
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.ELVEN, "Beren and Luthien");
        addKeyword(Keyword.TALE);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game, Culture.ELVEN, CardType.ALLY)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.ELVEN, CardType.ALLY));
            action.appendEffect(
                    new DiscardBottomCardFromDeckEffect(self, playerId, 1, false) {
                        @Override
                        protected void cardsDiscardedCallback(Collection<PhysicalCard> cards) {
                            for (final PhysicalCard card : cards)
                                if (card.getBlueprint().getCulture() == Culture.ELVEN) {
                                    action.appendEffect(
                                            new ChooseActiveCardEffect(self, playerId, "Choose a minion", CardType.MINION, Filters.inSkirmishAgainst(Race.ELF)) {
                                                @Override
                                                protected void cardSelected(LotroGame game, PhysicalCard minion) {
                                                    action.insertEffect(
                                                            new AddUntilEndOfPhaseModifierEffect(
                                                                    new StrengthModifier(self, Filters.sameCard(minion), -1)));
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
