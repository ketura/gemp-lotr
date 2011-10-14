package com.gempukku.lotro.cards.set3.gandalf;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
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
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. Response: If a companion is about to take a wound, spot 4 twilight tokens
 * and exert Gandalf to prevent that wound.
 */
public class Card3_036 extends AbstractPermanent {
    public Card3_036() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.GANDALF, Zone.SUPPORT, "Unknown Perils");
    }

    @Override
    public List<? extends Action> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (effect.getType() == EffectResult.Type.WOUND
                && game.getGameState().getTwilightPool() >= 4
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.name("Gandalf"))) {
            final WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            Collection<PhysicalCard> woundedCharacters = woundEffect.getAffectedCardsMinusPrevented(game);
            if (Filters.filter(woundedCharacters, game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION)).size() > 0) {
                final ActivateCardAction action = new ActivateCardAction(self);
                action.appendCost(
                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.name("Gandalf")));
                action.appendEffect(
                        new ChooseActiveCardEffect(self, playerId, "Choose a companion", Filters.in(woundedCharacters), Filters.type(CardType.COMPANION)) {
                            @Override
                            protected void cardSelected(PhysicalCard card) {
                                action.insertEffect(
                                        new PreventCardEffect(woundEffect, card));
                            }
                        });
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
