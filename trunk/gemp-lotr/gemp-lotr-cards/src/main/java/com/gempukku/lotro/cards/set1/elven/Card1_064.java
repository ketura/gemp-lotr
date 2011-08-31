package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.modifiers.AllyOnCurrentSiteModifier;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.CompositeModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Maneuver: Exert an Elf ally whose home is site 3. Until the regroup phase, that ally is strength +3 and
 * participates in archery fire and skirmishes.
 */
public class Card1_064 extends AbstractLotroCardBlueprint {
    public Card1_064() {
        super(Side.FREE_PEOPLE, CardType.EVENT, Culture.ELVEN, "Support of the Last Homely House", "1_64");
        addKeyword(Keyword.MANEUVER);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canPlayFPCardDuringPhase(game, Phase.MANEUVER, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ELF), Filters.type(CardType.ALLY), Filters.siteNumber(3), Filters.canExert())) {
            final CostToEffectAction action = new CostToEffectAction(self, Keyword.MANEUVER, "Exert an Elf ally whose home is site 3. Until the regroup phase, that ally is strength +3 and participates in archery fire and skirmishes.");
            action.addCost(
                    new ChooseActiveCardEffect(playerId, "Choose and Elf ally", Filters.keyword(Keyword.ELF), Filters.type(CardType.ALLY), Filters.siteNumber(3), Filters.canExert()) {
                        @Override
                        protected void cardSelected(PhysicalCard elfAlly) {
                            action.addCost(new ExertCharacterEffect(elfAlly));

                            List<Modifier> modifiers = new LinkedList<Modifier>();
                            modifiers.add(new StrengthModifier(null, null, 3));
                            modifiers.add(new AllyOnCurrentSiteModifier(null, null));

                            action.addEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new CompositeModifier(self, Filters.sameCard(elfAlly), modifiers)
                                            , Phase.REGROUP));
                        }
                    }
            );

            return Collections.singletonList(action);
        }
        return null;
    }
}
