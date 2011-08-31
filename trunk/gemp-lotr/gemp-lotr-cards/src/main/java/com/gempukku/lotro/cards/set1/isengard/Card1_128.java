package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
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
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Skirmish: Make an Uruk-hai strength -1 and damage +1.
 */
public class Card1_128 extends AbstractLotroCardBlueprint {
    public Card1_128() {
        super(Side.SHADOW, CardType.EVENT, Culture.ISENGARD, "Lurtz's Battle Cry", "1_128");
        addKeyword(Keyword.SKIRMISH);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canPlayShadowCardDuringPhase(game, Phase.SKIRMISH, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.URUK_HAI))) {
            final PlayEventAction action = new PlayEventAction(self);

            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose an Uruk-hai", Filters.keyword(Keyword.URUK_HAI)) {
                        @Override
                        protected void cardSelected(PhysicalCard urukHai) {
                            List<Modifier> modifiers = new LinkedList<Modifier>();
                            modifiers.add(new StrengthModifier(null, null, -1));
                            modifiers.add(new KeywordModifier(null, null, Keyword.DAMAGE));

                            action.addEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new CompositeModifier(self, Filters.sameCard(urukHai), modifiers), Phase.SKIRMISH));
                        }
                    }
            );

            return Collections.singletonList(action);
        }

        return null;
    }
}
