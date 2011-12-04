package com.gempukku.lotro.cards.set11.elven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.actions.ActivateCardAction;

import java.util.List;
import java.util.Collections;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 6
 * Type: Condition • Support Area
 * Game Text: To play, spot an Elf. Toil 2. (For each [ELVEN] character you exert when playing this, its twilight cost
 * is -2.) Archery: Discard this condition to make the fellowship archery total +2.
 */
public class Card11_017 extends AbstractPermanent {
    public Card11_017() {
        super(Side.FREE_PEOPLE, 6, CardType.CONDITION, Culture.ELVEN, Zone.SUPPORT, "Elven Marksmanship");
        addKeyword(Keyword.TOIL, 2);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Race.ELF);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.ARCHERY, self)
            && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action= new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new ArcheryTotalModifier(self, Side.FREE_PEOPLE, 2), Phase.ARCHERY));
            return Collections.singletonList(action);
        }
        return null;
    }
}
