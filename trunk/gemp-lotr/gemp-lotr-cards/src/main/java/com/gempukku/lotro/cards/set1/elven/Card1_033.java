package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Possession � Ranged Weapon
 * Strength: +1
 * Game Text: Bearer must be Legolas. Each time Legolas wins a skirmish, you may wound a minion.
 */
public class Card1_033 extends AbstractAttachableFPPossession {
    public Card1_033() {
        super(1, Culture.ELVEN, "Bow of the Galadhrim", true);
        addKeyword(Keyword.RANGED_WEAPON);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        Filter validTargetFilter = Filters.and(Filters.name("Legolas"), Filters.not(Filters.hasAttached(Filters.keyword(Keyword.RANGED_WEAPON))));

        appendAttachCardAction(actions, game, self, validTargetFilter);
        appendTransferPossessionAction(actions, game, self, validTargetFilter);

        return actions;
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new StrengthModifier(self, Filters.attachedTo(self), 1);
    }

    @Override
    public List<? extends Action> getOptionalOneTimeActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.winsSkirmish(effectResult, self.getAttachedTo())) {
            final CostToEffectAction action = new CostToEffectAction(self, null, "Wound a minion");
            action.addCost(
                    new ChooseActiveCardEffect(playerId, "Choose a minion", Filters.type(CardType.MINION)) {
                        @Override
                        protected void cardSelected(PhysicalCard minion) {
                            action.addEffect(new WoundCharacterEffect(minion));
                        }
                    }
            );
            return Collections.singletonList(action);
        }
        return null;
    }
}
