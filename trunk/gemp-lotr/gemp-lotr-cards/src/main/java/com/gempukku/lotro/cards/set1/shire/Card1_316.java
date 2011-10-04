package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Stealth. To play, exert a Hobbit. Plays to your support area. Each time the fellowship moves, spot 2
 * Hobbit companions to make the shadow number -1 (or spot 4 to make it -2).
 */
public class Card1_316 extends AbstractPermanent {
    public Card1_316() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.SHIRE, Zone.FREE_SUPPORT, "A Talent for Not Being Seen", true);
        addKeyword(Keyword.STEALTH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.HOBBIT));
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayPermanentAction action = super.getPlayCardAction(playerId, game, self, twilightModifier);
        action.appendCost(new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.race(Race.HOBBIT)));
        return action;
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new AbstractModifier(self, "Spot 2 Hobbit companions to make the shadow number -1 (or spot 4 to make it -2)",
                Filters.and(
                        Filters.type(CardType.SITE),
                        new Filter() {
                            @Override
                            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                return gameState.getCurrentSite() == physicalCard;
                            }
                        }
                ), new ModifierEffect[]{ModifierEffect.TWILIGHT_COST_MODIFIER}) {
            @Override
            public int getTwilightCost(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
                int hobbitsCount = Filters.countSpottable(gameState, modifiersQuerying, Filters.type(CardType.COMPANION), Filters.race(Race.HOBBIT));
                if (hobbitsCount >= 4)
                    return result - 2;
                if (hobbitsCount >= 2)
                    return result - 1;
                return result;
            }
        };
    }
}
