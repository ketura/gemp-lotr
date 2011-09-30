package com.gempukku.lotro.cards.set3.gandalf;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.costs.ChooseAndExertCharactersCost;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
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
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. Skirmish: Exert Gandalf to make a companion strength +1 for each companion
 * with the Gandalf signet you can spot (limit +3).
 */
public class Card3_035 extends AbstractPermanent {
    public Card3_035() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.GANDALF, Zone.FREE_SUPPORT, "Trust Me as you Once Did");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.name("Gandalf"))) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.SKIRMISH);
            action.appendCost(
                    new ChooseAndExertCharactersCost(action, playerId, 1, 1, Filters.name("Gandalf")));
            action.appendEffect(
                    new ChooseActiveCardEffect(playerId, "Chooose a companion", Filters.type(CardType.COMPANION)) {
                        @Override
                        protected void cardSelected(PhysicalCard card) {
                            int gandalfSignet = Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), new com.gempukku.lotro.filters.Filter[]{Filters.type(CardType.COMPANION), Filters.signet(Signet.GANDALF)});
                            int bonus = Math.min(3, gandalfSignet);
                            action.insertEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.sameCard(card), bonus), Phase.SKIRMISH));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
