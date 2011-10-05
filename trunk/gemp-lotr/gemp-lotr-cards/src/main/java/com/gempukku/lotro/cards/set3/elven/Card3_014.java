package com.gempukku.lotro.cards.set3.elven;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.PreventEffect;
import com.gempukku.lotro.common.*;
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
 * Type: Ally • Home 3 • Elf
 * Strength: 5
 * Vitality: 2
 * Site: 3
 * Game Text: To play, spot an Elf. Response: If an Elf is about to take a wound from a [SAURON] minion or
 * [SAURON] event, exert Erestor to prevent that wound.
 */
public class Card3_014 extends AbstractAlly {
    public Card3_014() {
        super(2, Block.FELLOWSHIP, 3, 5, 2, Race.ELF, Culture.ELVEN, "Erestor", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.ELF));
    }

    @Override
    public List<? extends Action> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (effect.getType() == EffectResult.Type.WOUND) {
            final WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            Collection<PhysicalCard> woundedCharacters = woundEffect.getAffectedCardsMinusPrevented(game);
            Collection<PhysicalCard> woundSources = woundEffect.getSources();
            if (Filters.filter(woundedCharacters, game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.ELF)).size() > 0) {
                if (woundSources != null && Filters.filter(woundSources, game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), Filters.or(Filters.type(CardType.MINION), Filters.type(CardType.EVENT))).size() > 0) {
                    final ActivateCardAction action = new ActivateCardAction(self, Keyword.RESPONSE);
                    action.appendCost(
                            new ExertCharactersEffect(self, self));
                    action.appendEffect(
                            new ChooseActiveCardEffect(self, playerId, "Choose an Elf to preventAll wound to", Filters.in(woundedCharacters), Filters.race(Race.ELF)) {
                                @Override
                                protected void cardSelected(PhysicalCard card) {
                                    action.insertEffect(
                                            new PreventEffect(woundEffect, card));
                                }
                            });
                    return Collections.singletonList(action);
                }
            }
        }
        return null;
    }
}
