package com.gempukku.lotro.cards.set11.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Twilight Cost: 3
 * Type: Site
 * Game Text: Forest. Each time you play an artifact or possession on your companion, you may draw a card.
 */
public class Card11_260 extends AbstractNewSite {
    public Card11_260() {
        super("Trollshaw Forest", 3, Direction.RIGHT);
        addKeyword(Keyword.FOREST);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Filters.or(CardType.ARTIFACT, CardType.POSSESSION))) {
            PlayCardResult playResult = (PlayCardResult) effectResult;
            if (playResult.getAttachedTo() != null && Filters.and(CardType.COMPANION, Filters.owner(playerId)).accepts(game.getGameState(), game.getModifiersQuerying(), playResult.getAttachedTo())) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                action.appendEffect(
                        new DrawCardsEffect(playerId, 1));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
