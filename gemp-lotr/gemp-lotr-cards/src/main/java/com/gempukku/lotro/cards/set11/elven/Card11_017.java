package com.gempukku.lotro.cards.set11.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

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
        super(Side.FREE_PEOPLE, 6, CardType.CONDITION, Culture.ELVEN, "Elven Marksmanship");
        addKeyword(Keyword.TOIL, 2);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Race.ELF);
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.ARCHERY, self)
            && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action= new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new ArcheryTotalModifier(self, Side.FREE_PEOPLE, 2)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
