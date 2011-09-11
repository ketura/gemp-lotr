package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
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
    public List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.MANEUVER, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.or(Filters.name("Arwen"), Filters.name("Aragorn")), Filters.canExert())) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.MANEUVER, "Exert Aragorn to heal Arwen, or exert Arwen to heal Aragorn.");

            List<Effect> possibleEffects = new LinkedList<Effect>();

            final PhysicalCard arwen = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name("Arwen"));
            final PhysicalCard aragorn = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name("Aragorn"));

            if (arwen != null) {
                action.addCost(
                        new ExertCharacterEffect(arwen) {
                            @Override
                            public void playEffect(LotroGame game) {
                                super.playEffect(game);
                                if (aragorn != null)
                                    new HealCharacterEffect(aragorn);
                            }
                        });
            }
            if (aragorn != null) {
                action.addCost(
                        new ExertCharacterEffect(aragorn) {
                            @Override
                            public void playEffect(LotroGame game) {
                                super.playEffect(game);
                                if (arwen != null)
                                    new HealCharacterEffect(arwen);
                            }
                        });
            }

            action.addCost(
                    new ChoiceEffect(action, playerId, possibleEffects, true));
            return Collections.singletonList(action);
        }
        return null;
    }
}
