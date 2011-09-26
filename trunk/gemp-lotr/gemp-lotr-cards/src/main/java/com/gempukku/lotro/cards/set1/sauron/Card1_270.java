package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.costs.ExertCharactersCost;
import com.gempukku.lotro.cards.effects.CardAffectsCardEffect;
import com.gempukku.lotro.cards.modifiers.RoamingPenaltyModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 8
 * Vitality: 2
 * Site: 6
 * Game Text: Tracker. The roaming penalty for each [SAURON] minion you play is -1. Skirmish: Exert this minion to wound
 * a character it is skirmishing.
 */
public class Card1_270 extends AbstractMinion {
    public Card1_270() {
        super(3, 8, 2, 6, Race.ORC, Culture.SAURON, "Orc Scouting Band");
        addKeyword(Keyword.TRACKER);
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new RoamingPenaltyModifier(self, Filters.and(Filters.owner(self.getOwner()), Filters.culture(Culture.SAURON), Filters.type(CardType.MINION)), -1);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self, 0)
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self)) {
            ActivateCardAction action = new ActivateCardAction(self, Keyword.SKIRMISH, "Exert this minion to wound a character he is skirmishing.");
            action.appendCost(
                    new ExertCharactersCost(playerId, self));
            Skirmish skirmish = game.getGameState().getSkirmish();
            if (skirmish != null && skirmish.getShadowCharacters().contains(self)) {
                PhysicalCard fpChar = skirmish.getFellowshipCharacter();
                if (fpChar != null) {
                    action.appendEffect(new CardAffectsCardEffect(self, fpChar));
                    action.appendEffect(new WoundCharacterEffect(playerId, fpChar));
                }
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
