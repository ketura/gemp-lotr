package com.gempukku.lotro.game.rules.tribbles;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.game.actions.tribbles.TribblesPlayPermanentAction;
import com.gempukku.lotro.game.rules.PlayUtils;

public class TribblesPlayUtils extends PlayUtils {
    private static Zone getPlayToZone(PhysicalCard card) {
        return Zone.PLAY_PILE;
    }

    public static CostToEffectAction getPlayCardAction(DefaultGame game, PhysicalCard card,
                                                       boolean ignoreRoamingPenalty) {
        TribblesPlayPermanentAction action =
                new TribblesPlayPermanentAction(card, getPlayToZone(card));
        game.getModifiersQuerying().appendExtraCosts(game, action, card);
        game.getModifiersQuerying().appendPotentialDiscounts(game, action, card);
        return action;
    }

    public static boolean checkPlayRequirements(DefaultGame game, PhysicalCard card) {

        // Check if card's own play requirements are met
        if (!card.getBlueprint().checkPlayRequirements(game, card))
            return false;

        // Check if the card's playability has been modified in the current game state
        if (!game.getModifiersQuerying().canPlayCard(game, card.getOwner(), card))
            return false;

        // Otherwise, the play requirements are met if the card is next in the tribble sequence
        // This function shouldn't be used anymore since checkPlayRequirements has been moved to TribblesGame
        return true;
    }
}