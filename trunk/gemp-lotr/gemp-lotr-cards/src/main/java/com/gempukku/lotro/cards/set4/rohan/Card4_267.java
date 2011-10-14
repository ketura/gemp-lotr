package com.gempukku.lotro.cards.set4.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 6
 * Signet: Theoden
 * Game Text: While you can spot a [ROHAN] Man, Eomer's twilight cost is -1. Eomer is strength +2 for each wound
 * on each minion he is skirmishing.
 */
public class Card4_267 extends AbstractCompanion {
    public Card4_267() {
        super(3, 7, 3, Culture.ROHAN, Race.MAN, Signet.THÉODEN, "Eomer", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        twilightModifier -= (PlayConditions.canSpot(game, Filters.culture(Culture.ROHAN), Filters.race(Race.MAN))) ? 1 : 0;
        return super.checkPlayRequirements(playerId, game, self, twilightModifier);
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        twilightModifier -= (PlayConditions.canSpot(game, Filters.culture(Culture.ROHAN), Filters.race(Race.MAN))) ? 1 : 0;
        return super.getPlayCardAction(playerId, game, self, twilightModifier);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.sameCard(self), null,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
                                final Collection<PhysicalCard> minions = Filters.filterActive(gameState, modifiersQuerying, Filters.inSkirmishAgainst(Filters.sameCard(self)));
                                int wounds = 0;
                                for (PhysicalCard minion : minions)
                                    wounds += gameState.getWounds(minion);
                                return 2 * wounds;
                            }
                        }));
    }
}
