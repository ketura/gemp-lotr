package com.gempukku.lotro.cards.set1.gondor;

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

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Make a [GONDOR] companion strength +2 (or +4 if he is defender +1).
 */
public class Card1_116 extends AbstractEvent {
    public Card1_116() {
        super(Side.FREE_PEOPLE, Culture.GONDOR, "Swordarm of the White Tower", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);

        action.addEffect(
                new ChooseActiveCardEffect(playerId, "Choose a GONDOR companion", Filters.culture(Culture.GONDOR), Filters.type(CardType.COMPANION)) {
                    @Override
                    protected void cardSelected(PhysicalCard gondorCompanion) {
                        boolean isDefender = game.getModifiersQuerying().hasKeyword(game.getGameState(), gondorCompanion, Keyword.DEFENDER);
                        int bonus = isDefender ? 4 : 2;
                        action.addEffect(new CardAffectsCardEffect(self, gondorCompanion));
                        action.addEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(gondorCompanion), bonus), Phase.SKIRMISH));
                    }
                });
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
