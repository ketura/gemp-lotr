package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 2
 * Type: Site
 * Site: 4
 * Game Text: Underground. When the fellowship moves to Dwarrowdelf Chamber, Gimli or 2 other companions must exert.
 */
public class Card1_344 extends AbstractSite {
    public Card1_344() {
        super("Dwarrowdelf Chamber", 4, 2, Direction.RIGHT);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(final LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_MOVE_TO
                && game.getGameState().getCurrentSite() == self) {
            String fpPlayerId = game.getGameState().getCurrentPlayerId();
            boolean gimliCanExert = Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name("Gimli"), Filters.canExert());
            boolean twoOtherCanExert = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.not(Filters.name("Gimli")), Filters.type(CardType.COMPANION), Filters.canExert()) >= 2;
            if (gimliCanExert && twoOtherCanExert) {
                final RequiredTriggerAction action = new RequiredTriggerAction(self, null, "Gimli or 2 other companions must exert.");

                PhysicalCard gimli = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name("Gimli"));
                List<Effect> possibleEffects = new LinkedList<Effect>();
                possibleEffects.add(new ExertCharacterEffect(game.getGameState().getCurrentPlayerId(), gimli));
                possibleEffects.add(
                        new ChooseActiveCardsEffect(fpPlayerId, "Choose two companions to exert", 2, 2, Filters.not(Filters.name("Gimli")), Filters.type(CardType.COMPANION), Filters.canExert()) {
                            @Override
                            protected void cardsSelected(List<PhysicalCard> cards) {
                                action.addEffect(new ExertCharacterEffect(game.getGameState().getCurrentPlayerId(), Filters.in(cards)));
                            }
                        });
                action.addEffect(
                        new ChoiceEffect(action, fpPlayerId, possibleEffects, false));
                return Collections.singletonList(action);
            }
            if (gimliCanExert) {
                RequiredTriggerAction action = new RequiredTriggerAction(self, null, "Gimli must exert");
                PhysicalCard gimli = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name("Gimli"));
                action.addEffect(new ExertCharacterEffect(game.getGameState().getCurrentPlayerId(), gimli));
                return Collections.singletonList(action);
            }
            if (twoOtherCanExert) {
                final RequiredTriggerAction action = new RequiredTriggerAction(self, null, "2 non-Gimli companions must exert");
                action.addEffect(
                        new ChooseActiveCardsEffect(fpPlayerId, "Choose two companions to exert", 2, 2, Filters.not(Filters.name("Gimli")), Filters.type(CardType.COMPANION), Filters.canExert()) {
                            @Override
                            protected void cardsSelected(List<PhysicalCard> cards) {
                                action.addEffect(new ExertCharacterEffect(game.getGameState().getCurrentPlayerId(), Filters.in(cards)));
                            }
                        });

                return Collections.singletonList(action);
            }
        }

        return null;
    }
}
