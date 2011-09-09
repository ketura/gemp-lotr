package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;
import com.gempukku.lotro.logic.modifiers.CompositeModifier;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be Boromir. He is damage +1. Skirmish: Exert Boromir to wound an Orc or Uruk-hai he is
 * skirmishing.
 */
public class Card1_095 extends AbstractAttachableFPPossession {
    public Card1_095() {
        super(1, Culture.GONDOR, Keyword.HAND_WEAPON, "Blade of Gondor", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Boromir");
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(new StrengthModifier(null, null, 2));
        modifiers.add(new KeywordModifier(null, null, Keyword.DAMAGE));
        return new CompositeModifier(self, Filters.attachedTo(self), modifiers);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self.getAttachedTo())) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.SKIRMISH, "Exert Boromir to wound an Orc or Uruk-hai he is skirmishing.");
            action.addCost(
                    new ExertCharacterEffect(self.getAttachedTo()));
            Skirmish skirmish = game.getGameState().getSkirmish();
            if (skirmish != null && skirmish.getFellowshipCharacter() == self.getAttachedTo()) {
                action.addEffect(
                        new ChooseActiveCardEffect(playerId, "Chose Orc or Uruk-hai in skirmish", Filters.or(Filters.keyword(Keyword.ORC), Filters.keyword(Keyword.URUK_HAI)), Filters.in(skirmish.getShadowCharacters())) {
                            @Override
                            protected void cardSelected(PhysicalCard minion) {
                                action.addEffect(new WoundCharacterEffect(minion));
                            }
                        });
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
