package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
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
import com.gempukku.lotro.logic.timing.Action;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Stealth. To play, exert a Hobbit. Plays to your support area. Each time the fellowship moves, spot 2
 * Hobbit companions to make the shadow number -1 (or spot 4 to make it -2).
 */
public class Card1_316 extends AbstractLotroCardBlueprint {
    public Card1_316() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, Culture.SHIRE, "A Talent for Not Being Seen", true);
        addKeyword(Keyword.STEALTH);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        return PlayConditions.checkUniqueness(game.getGameState(), game.getModifiersQuerying(), self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.HOBBIT), Filters.canExert());
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayPermanentAction action = new PlayPermanentAction(self, Zone.FREE_SUPPORT);
        action.addCost(new ChooseAndExertCharacterEffect(action, playerId, "Choose a Hobbit", true, Filters.keyword(Keyword.HOBBIT), Filters.canExert()));
        return action;
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
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
                int hobbitsCount = Filters.countActive(gameState, modifiersQuerying, Filters.type(CardType.COMPANION), Filters.keyword(Keyword.HOBBIT));
                if (hobbitsCount >= 4)
                    return result - 2;
                if (hobbitsCount >= 2)
                    return result - 1;
                return result;
            }
        };
    }
}
