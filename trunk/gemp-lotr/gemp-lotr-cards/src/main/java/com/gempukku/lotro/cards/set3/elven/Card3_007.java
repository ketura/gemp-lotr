package com.gempukku.lotro.cards.set3.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.cards.effects.PreventEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Signet: Frodo
 * Game Text: Ranger. Response: If the Ring-bearer is about to take a wound, discard 3 cards from hand to prevent
 * that wound.
 */
public class Card3_007 extends AbstractCompanion {
    public Card3_007() {
        super(2, 6, 3, Culture.ELVEN, Race.ELF, Signet.FRODO, "Arwen", true);
        addKeyword(Keyword.RANGER);
    }

    @Override
    public List<? extends Action> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (effect.getType() == EffectResult.Type.WOUND
                && game.getGameState().getHand(playerId).size() >= 3) {
            final WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            Collection<PhysicalCard> woundedCharacters = woundEffect.getAffectedCardsMinusPrevented(game);
            if (Filters.filter(woundedCharacters, game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.RING_BEARER)).size() > 0) {
                final ActivateCardAction action = new ActivateCardAction(self);
                action.appendCost(
                        new ChooseAndDiscardCardsFromHandEffect(action, playerId, 3));
                action.appendEffect(
                        new ChooseActiveCardEffect(self, playerId, "Choose character to preventAll wound", Filters.in(woundedCharacters), Filters.keyword(Keyword.RING_BEARER)) {
                            @Override
                            protected void cardSelected(PhysicalCard card) {
                                action.insertEffect(
                                        new PreventEffect(woundEffect, card));
                            }
                        });
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
