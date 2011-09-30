package com.gempukku.lotro.cards.set2.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.costs.DiscardCardsFromPlayCost;
import com.gempukku.lotro.cards.effects.PreventEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Stealth. Plays to your support area. Response: If a Hobbit is about to take a wound, discard this
 * condition to prevent that wound.
 */
public class Card2_106 extends AbstractPermanent {
    public Card2_106() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.SHIRE, Zone.FREE_SUPPORT, "Nice Imitation");
        addKeyword(Keyword.STEALTH);
    }

    @Override
    public List<? extends Action> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (effect.getType() == EffectResult.Type.WOUND) {
            final WoundCharacterEffect woundEffect = (WoundCharacterEffect) effect;
            Collection<PhysicalCard> woundedCharacters = woundEffect.getCardsToBeAffected(game);
            if (Filters.filter(woundedCharacters, game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.HOBBIT)).size() > 0) {
                final ActivateCardAction action = new ActivateCardAction(self, Keyword.RESPONSE);
                action.appendCost(
                        new DiscardCardsFromPlayCost(self));
                action.appendEffect(
                        new ChooseActiveCardEffect(playerId, "Choose Hobbit", Filters.in(woundedCharacters), Filters.race(Race.HOBBIT)) {
                            @Override
                            protected void cardSelected(PhysicalCard card) {
                                action.insertEffect(new PreventEffect(woundEffect, card));
                            }
                        });
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
