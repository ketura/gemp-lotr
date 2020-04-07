package com.gempukku.lotro.cards.set13.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Event • Archery
 * Game Text: Spot 2 Elf archers to make the fellowship archery total +1 (or +2 if a Shadow player has replaced the
 * fellowship’s current site this turn).
 */
public class Card13_021 extends AbstractEvent {
    public Card13_021() {
        super(Side.FREE_PEOPLE, 2, Culture.ELVEN, "Many Miles", Phase.ARCHERY);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 2, Race.ELF, Keyword.ARCHER);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        int total = game.getModifiersQuerying().hasFlagActive(game, ModifierFlag.SHADOW_PLAYER_REPLACED_CURRENT_SITE) ? 2 : 1;
        action.appendEffect(
                new AddUntilEndOfPhaseModifierEffect(
                        new ArcheryTotalModifier(self, Side.FREE_PEOPLE, total)));
        return action;
    }
}
