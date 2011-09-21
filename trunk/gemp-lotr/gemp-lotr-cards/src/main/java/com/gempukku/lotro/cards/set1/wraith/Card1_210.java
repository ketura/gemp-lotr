package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.CardAffectsCardEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.CompositeModifier;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Spot 3 burdens to make a Nazgul strength +1 and damage +1, or spot 6 burdens to make a Nazgul
 * strength +3 and damage +2.
 */
public class Card1_210 extends AbstractEvent {
    public Card1_210() {
        super(Side.SHADOW, Culture.WRAITH, "Dark Whispers", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && game.getGameState().getBurdens() >= 3;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.addEffect(
                new ChooseActiveCardEffect(playerId, "Choose a Nazgul", Filters.race(Race.NAZGUL)) {
                    @Override
                    protected void cardSelected(PhysicalCard nazgul) {
                        int burdens = game.getGameState().getBurdens();

                        List<Modifier> modifiers = new LinkedList<Modifier>();
                        modifiers.add(new StrengthModifier(null, null, (burdens >= 6) ? 3 : 1));
                        modifiers.add(new KeywordModifier(null, null, Keyword.DAMAGE, (burdens >= 6) ? 2 : 1));
                        action.addEffect(new CardAffectsCardEffect(self, nazgul));
                        action.addEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new CompositeModifier(self, Filters.sameCard(nazgul), modifiers), Phase.SKIRMISH));
                    }
                });
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
