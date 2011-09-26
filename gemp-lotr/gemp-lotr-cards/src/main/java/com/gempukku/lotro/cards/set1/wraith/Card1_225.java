package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.costs.ExertCharactersCost;
import com.gempukku.lotro.cards.effects.CardAffectsCardEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.CompositeModifier;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 2
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be Ulaire Lemenya. He is damage +1. Skirmish: Exert Ulaire Lemenya to discard a possession
 * borne by a character he is skirmishing.
 */
public class Card1_225 extends AbstractAttachable {
    public Card1_225() {
        super(Side.SHADOW, CardType.POSSESSION, 2, Culture.WRAITH, Keyword.HAND_WEAPON, "Sword of Minas Morgul", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Ulaire Lemenya");
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(new StrengthModifier(null, null, 2));
        modifiers.add(new KeywordModifier(null, null, Keyword.DAMAGE));
        return new CompositeModifier(self, Filters.hasAttached(self), modifiers);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self, 0)
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self.getAttachedTo())) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.SKIRMISH);
            action.appendCost(new ExertCharactersCost(playerId, self.getAttachedTo()));
            Skirmish skirmish = game.getGameState().getSkirmish();
            if (skirmish != null
                    && skirmish.getShadowCharacters().contains(self.getAttachedTo())) {
                action.appendEffect(
                        new ChooseActiveCardEffect(playerId, "Choose possession borne by character he is skirmishing", Filters.type(CardType.POSSESSION), Filters.hasAttached(skirmish.getFellowshipCharacter())) {
                            @Override
                            protected void cardSelected(PhysicalCard possession) {
                                action.appendEffect(new CardAffectsCardEffect(self, possession));
                                action.appendEffect(
                                        new DiscardCardsFromPlayEffect(self, possession));
                            }
                        });
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
