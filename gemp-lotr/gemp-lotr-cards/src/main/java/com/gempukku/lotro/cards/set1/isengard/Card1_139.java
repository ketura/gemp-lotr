package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
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
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Make an Uruk-hai strength +2, or spot 5 companions to make an Uruk-hai strength +4 and fierce
 * until the regroup phase.
 */
public class Card1_139 extends AbstractLotroCardBlueprint {
    public Card1_139() {
        super(Side.SHADOW, CardType.EVENT, Culture.ISENGARD, "Savagery to Match Their Numbers");
        addKeyword(Keyword.SKIRMISH);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canPlayShadowCardDuringPhase(game, Phase.SKIRMISH, self)) {
            final PlayEventAction action = new PlayEventAction(self);
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose an Uruk-hai", Filters.keyword(Keyword.URUK_HAI)) {
                        @Override
                        protected void cardSelected(PhysicalCard urukHai) {
                            if (Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION)) >= 5) {
                                List<Modifier> modifiers = new LinkedList<Modifier>();
                                modifiers.add(new StrengthModifier(null, null, 4));
                                modifiers.add(new KeywordModifier(null, null, Keyword.FIERCE));
                                action.addEffect(
                                        new AddUntilStartOfPhaseModifierEffect(
                                                new CompositeModifier(self, Filters.sameCard(urukHai), modifiers), Phase.REGROUP));
                            } else {
                                action.addEffect(
                                        new AddUntilEndOfPhaseModifierEffect(
                                                new StrengthModifier(self, Filters.sameCard(urukHai), 2), Phase.SKIRMISH));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }

        return null;
    }
}
