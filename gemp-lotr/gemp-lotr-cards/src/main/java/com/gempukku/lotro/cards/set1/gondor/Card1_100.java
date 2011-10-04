package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Tale. Plays to your support area. Maneuver: Exert Aragorn to heal Arwen, or exert Arwen to heal Aragorn.
 */
public class Card1_100 extends AbstractPermanent {
    public Card1_100() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GONDOR, Zone.FREE_SUPPORT, "The Choice of Luthien", true);
        addKeyword(Keyword.TALE);
    }

    @Override
    public List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.MANEUVER, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.or(Filters.name("Arwen"), Filters.name("Aragorn")))) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.MANEUVER);

            List<Effect> possibleEffects = new LinkedList<Effect>();

            final PhysicalCard arwen = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name("Arwen"));
            final PhysicalCard aragorn = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name("Aragorn"));

            if (arwen != null) {
                possibleEffects.add(
                        new ExertCharactersEffect(self, arwen) {
                            @Override
                            protected FullEffectResult playEffectReturningResult(LotroGame game) {
                                FullEffectResult effectResult = super.playEffectReturningResult(game);
                                if (effectResult.isSuccessful() && aragorn != null) {
                                    action.appendEffect(new HealCharactersEffect(playerId, aragorn));
                                }
                                return effectResult;
                            }
                        });
            }
            if (aragorn != null) {
                possibleEffects.add(
                        new ExertCharactersEffect(self, aragorn) {
                            @Override
                            protected FullEffectResult playEffectReturningResult(LotroGame game) {
                                FullEffectResult effectResult = super.playEffectReturningResult(game);
                                if (effectResult.isSuccessful() && arwen != null) {
                                    action.appendEffect(new HealCharactersEffect(playerId, arwen));
                                }
                                return effectResult;    //To change body of overridden methods use File | Settings | File Templates.
                            }
                        });
            }

            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
