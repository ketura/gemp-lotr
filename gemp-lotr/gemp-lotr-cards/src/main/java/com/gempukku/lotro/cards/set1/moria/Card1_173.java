package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays to your support area. Each time you play a [MORIA] weapon, add (1). Response: If a [MORIA] Orc is
 * about to take a wound, discard this condition to prevent that wound.
 */
public class Card1_173 extends AbstractPermanent {
    public Card1_173() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.MORIA, Zone.SUPPORT, "Goblin Armory");
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), null, self, 0)
                && PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.and(Filters.owner(self.getOwner()), Filters.culture(Culture.MORIA), Filters.or(Filters.keyword(Keyword.HAND_WEAPON), Filters.keyword(Keyword.RANGED_WEAPON))))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(new AddTwilightEffect(self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Action> getOptionalBeforeActions(String playerId, LotroGame game, final Effect effect, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), null, self, 0)
                && effect.getType() == EffectResult.Type.WOUND) {
            final WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            final Collection<PhysicalCard> cardsToBeWounded = woundEffect.getAffectedCardsMinusPrevented(game);
            if (Filters.filter(cardsToBeWounded, game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.MORIA), Filters.race(Race.ORC)).size() > 0) {
                final ActivateCardAction action = new ActivateCardAction(self);
                action.appendCost(
                        new DiscardCardsFromPlayEffect(self, self));
                action.appendEffect(
                        new ChooseActiveCardEffect(self, playerId, "Choose MORIA Orc", Filters.culture(Culture.MORIA), Filters.race(Race.ORC), Filters.in(cardsToBeWounded)) {
                            @Override
                            protected void cardSelected(LotroGame game, PhysicalCard moriaOrc) {
                                action.appendEffect(
                                        new PreventCardEffect(woundEffect, moriaOrc));
                            }
                        });
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
