package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventFromHandAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Skirmish: Make an Elf strength +2 (or +4 if skirmishing a Nazgul).
 */
public class Card1_037 extends AbstractLotroCardBlueprint {
    public Card1_037() {
        super(Side.FREE_PEOPLE, CardType.EVENT, Culture.ELVEN, "Defiance", "1_37");
        addKeyword(Keyword.SKIRMISH);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canPlayFromHandDuringPhase(game.getGameState(), Phase.SKIRMISH, self)) {
            final PlayEventFromHandAction action = new PlayEventFromHandAction(self);

            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose an Elf", Filters.keyword(Keyword.ELF)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard elf) {
                            Skirmish skirmish = game.getGameState().getSkirmish();
                            int bonus = 2;
                            if (skirmish != null) {
                                if (skirmish.getFellowshipCharacter() == elf && Filters.filter(skirmish.getShadowCharacters(), game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.NAZGUL)).size() > 0)
                                    bonus = 4;
                            }

                            action.addEffect(new AddUntilEndOfPhaseModifierEffect(new StrengthModifier(self, Filters.sameCard(elf), bonus), Phase.SKIRMISH));
                        }
                    });

            return Collections.singletonList(action);
        }
        return null;
    }
}
