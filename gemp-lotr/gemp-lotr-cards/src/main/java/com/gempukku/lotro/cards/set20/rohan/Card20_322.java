package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.CardMatchesEvaluator;

/**
 * 1
 * Fey He Seemed
 * Rohan	Event • Skirmish
 * Make a [Rohan] Man strength +2 (or strength +3 and damage +1 if skirmishing a wounded minion).
 */
public class Card20_322 extends AbstractEvent {
    public Card20_322() {
        super(Side.FREE_PEOPLE, 1, Culture.ROHAN, "Fey He Seemed", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, final LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new CardMatchesEvaluator(2, 3, Filters.inSkirmishAgainst(CardType.MINION, Filters.wounded)), Culture.ROHAN, Race.MAN) {
                    @Override
                    protected void selectedCharacterCallback(PhysicalCard selectedCharacter) {
                        if (Filters.and(Filters.inSkirmishAgainst(CardType.MINION, Filters.wounded)).accepts(game, selectedCharacter))
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new KeywordModifier(self, selectedCharacter, Keyword.DAMAGE, 1)));
                    }
                });
        return action;
    }
}
