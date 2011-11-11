package com.gempukku.lotro.cards.set8.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.RemoveTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Fellowship: Exert a Dwarf companion and either an [ELVEN] companion or a [GONDOR] Man to add a [DWARVEN]
 * token here. Skirmish: Remove a [DWARVEN] token here to make a companion damage +1.
 */
public class Card8_008 extends AbstractPermanent {
    public Card8_008() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "Wish For Our Kinfolk");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game, Race.DWARF, CardType.COMPANION)
                && PlayConditions.canExert(self, game,
                Filters.or(
                        Filters.and(CardType.COMPANION, Culture.ELVEN),
                        Filters.and(Culture.GONDOR, Race.MAN)))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.DWARF, CardType.COMPANION));
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.or(
                            Filters.and(CardType.COMPANION, Culture.ELVEN),
                            Filters.and(Culture.GONDOR, Race.MAN))));
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.DWARVEN));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canRemoveTokens(game, Token.DWARVEN, 1, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTokenEffect(self, self, Token.DWARVEN));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a companion", CardType.COMPANION) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new KeywordModifier(self, card, Keyword.DAMAGE, 1), Phase.SKIRMISH));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
