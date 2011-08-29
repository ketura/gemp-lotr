package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundEffect;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Possession � Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be Gimli. He is damage +1. Each time Gimli wins a skirmish, you may wound an Orc.
 */
public class Card1_014 extends AbstractAttachableFPPossession {
    public Card1_014() {
        super(2, Culture.DWARVEN, "Gimli's Battle Axe", "1_14", true);
        addKeyword(Keyword.HAND_WEAPON);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        Filter validTargetFilter = Filters.and(Filters.name("Gimli"), Filters.not(Filters.attached(Filters.keyword(Keyword.HAND_WEAPON))));

        appendAttachCardFromHandAction(actions, game, self, validTargetFilter);

        appendTransferPossessionAction(actions, game, self, validTargetFilter);

        return actions;
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new AbstractModifier(self, "Strength +2, Damage +1", Filters.sameCard(self.getAttachedTo())) {
            @Override
            public int getStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
                return result + 2;
            }

            @Override
            public int getKeywordCount(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword, int result) {
                if (keyword == Keyword.DAMAGE)
                    return result + 1;
                else
                    return result;
            }

            @Override
            public boolean hasKeyword(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword, boolean result) {
                return (result || keyword == Keyword.DAMAGE);
            }
        };
    }

    @Override
    public List<? extends Action> getPlayableWhenActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.winsSkirmish(effectResult, self.getAttachedTo())) {
            if (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ORC))) {
                final CostToEffectAction action = new CostToEffectAction(self, "Wound an Orc");
                action.addEffect(
                        new ChooseActiveCardEffect(playerId, "Choose an Orc to wound", Filters.keyword(Keyword.ORC)) {
                            @Override
                            protected void cardSelected(LotroGame game, PhysicalCard orc) {
                                action.addEffect(new WoundEffect(orc));
                            }
                        }
                );

                return Collections.<Action>singletonList(action);
            }
        }

        return null;
    }
}
