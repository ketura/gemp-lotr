package com.gempukku.lotro.cards.set40.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.PlayersCantUseCardSpecialAbilitiesModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Title: One Ill Turn Deserves Another
 * Set: Second Edition
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Event - Maneuver
 * Card Number: 1C130
 * Game Text: Spell. Exert Saruman to exert a companion. That companion cannot use special abilities until the regroup phase.
 */
public class Card40_130 extends AbstractEvent {
    public Card40_130() {
        super(Side.SHADOW, 1, Culture.ISENGARD, "One Ill Turn Deserves Another", Phase.MANEUVER);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Filters.saruman);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.saruman));
        action.appendEffect(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION) {
                    @Override
                    protected void forEachCardExertedCallback(PhysicalCard character) {
                        action.appendEffect(
                                new AddUntilStartOfPhaseModifierEffect(
                                        new PlayersCantUseCardSpecialAbilitiesModifier(self, character), Phase.REGROUP));
                    }
                });
        return action;
    }
}
