package com.gempukku.lotro.cards.set11.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.CountActiveEvaluator;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event • Skirmish
 * Game Text: For each dwelling and forest site on the adventure path, make a Hobbit strength +1.
 */
public class Card11_161 extends AbstractEvent {
    public Card11_161() {
        super(Side.FREE_PEOPLE, 0, Culture.SHIRE, "Concerning Hobbits", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new CountActiveEvaluator(CardType.SITE, Zone.ADVENTURE_PATH, Filters.or(Keyword.DWELLING, Keyword.FOREST)), Race.HOBBIT));
        return action;
    }
}
