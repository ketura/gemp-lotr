package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Make a ranger strength +2 (or +4 when skirmishing a roaming minion).
 */
public class Card1_117 extends AbstractEvent {
    public Card1_117() {
        super(Side.FREE_PEOPLE, Culture.GONDOR, "Swordsman of the Northern Kingdom", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self, true);

        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a Ranger", Filters.keyword(Keyword.RANGER)) {
                    @Override
                    protected void cardSelected(PhysicalCard gondorCompanion) {
                        int bonus = 2;
                        Skirmish skirmish = game.getGameState().getSkirmish();
                        if (skirmish != null && skirmish.getFellowshipCharacter() == gondorCompanion
                                && Filters.filter(skirmish.getShadowCharacters(), game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ROAMING)).size() > 0)
                            bonus = 4;

                        action.appendEffect(
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
