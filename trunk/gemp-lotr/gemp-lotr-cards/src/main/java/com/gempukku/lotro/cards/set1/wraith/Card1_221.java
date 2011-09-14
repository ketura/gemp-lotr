package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.CompositeModifier;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 2
 * Type: Possession • Hand Weapon
 * Strength: +3
 * Game Text: Bearer must be The Witch-king. He is damage +1. Response: If The Witch-king wins a skirmish, exert him
 * to discard a Free Peoples condition.
 */
public class Card1_221 extends AbstractAttachable {
    public Card1_221() {
        super(Side.SHADOW, CardType.POSSESSION, 2, Culture.WRAITH, Keyword.HAND_WEAPON, "The Pale Blade", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("THe Witch-king");
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(new StrengthModifier(null, null, 3));
        modifiers.add(new KeywordModifier(null, null, Keyword.DAMAGE));
        return new CompositeModifier(self, Filters.attachedTo(self), modifiers);
    }

    @Override
    public List<? extends Action> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (PlayConditions.winsSkirmish(effectResult, self.getAttachedTo())
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self.getAttachedTo())) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Exert bearer to discard a Free Peoples condition.");
            action.addCost(
                    new ExertCharacterEffect(playerId, self.getAttachedTo()));
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose a Free Peoples condition", Filters.side(Side.FREE_PEOPLE), Filters.type(CardType.CONDITION)) {
                        @Override
                        protected void cardSelected(PhysicalCard condition) {
                            action.addEffect(
                                    new DiscardCardFromPlayEffect(self, condition));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
