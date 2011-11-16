package com.gempukku.lotro.cards.set10.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 5
 * Type: Minion • Man
 * Strength: 11
 * Vitality: 3
 * Site: 4
 * Game Text: Southron. At the start of each skirmish involving this minion, you may spot 6 companions and another
 * [RAIDER] Man to discard an unbound companion this minion is skirmishing.
 */
public class Card10_042 extends AbstractMinion {
    public Card10_042() {
        super(5, 11, 3, 4, Race.MAN, Culture.RAIDER, "Far Harad Mercenaries");
        addKeyword(Keyword.SOUTHRON);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.startOfPhase(game, effectResult, Phase.SKIRMISH)
                && Filters.inSkirmish.accepts(game.getGameState(), game.getModifiersQuerying(), self)
                && PlayConditions.canSpot(game, 6, CardType.COMPANION)
                && PlayConditions.canSpot(game, Filters.not(self), Culture.RAIDER, Race.MAN)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.unboundCompanion, Filters.inSkirmishAgainst(self)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
