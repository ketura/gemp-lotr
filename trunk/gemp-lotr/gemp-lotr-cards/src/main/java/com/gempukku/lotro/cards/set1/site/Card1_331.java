package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.CardAffectsCardEffect;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 2
 * Type: Site
 * Site: 2
 * Game Text: Plains. Skirmish: Exert your companion or minion to make that character strength +2.
 */
public class Card1_331 extends AbstractSite {
    public Card1_331() {
        super("Ettenmoors", 2, 2, Direction.LEFT);
        addKeyword(Keyword.PLAINS);
    }

    @Override
    public List<? extends Action> getPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.or(Filters.type(CardType.COMPANION), Filters.type(CardType.MINION)), Filters.owner(playerId))) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.SKIRMISH);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.or(Filters.type(CardType.COMPANION), Filters.type(CardType.MINION)), Filters.owner(playerId)) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> characters) {
                            super.cardsSelected(game, characters);    //To change body of overridden methods use File | Settings | File Templates.
                            if (characters.size() > 0) {
                                action.appendEffect(new CardAffectsCardEffect(self, characters));
                                action.appendEffect(
                                        new AddUntilEndOfPhaseModifierEffect(
                                                new StrengthModifier(self, Filters.in(characters), 2), Phase.SKIRMISH));

                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
