package com.gempukku.lotro.game.rules.tribbles;

import com.gempukku.lotro.cards.LotroCardBlueprint;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.lotronly.AttachPermanentAction;
import com.gempukku.lotro.game.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.game.actions.lotronly.PlayEventAction;
import com.gempukku.lotro.game.actions.lotronly.PlayPermanentAction;
import com.gempukku.lotro.game.rules.RuleUtils;
import com.gempukku.lotro.game.timing.PlayConditions;
import com.google.common.collect.ImmutableMap;
import com.gempukku.lotro.game.rules.PlayUtils;

import java.util.HashMap;
import java.util.Map;

public class TribblesPlayUtils extends PlayUtils {
    private static Zone getPlayToZone(PhysicalCard card) {
        return Zone.ADVENTURE_DECK;
    }

    public static Map<Phase, Keyword> PhaseKeywordMap = ImmutableMap.copyOf(new HashMap<>() {{
        put(Phase.FELLOWSHIP, Keyword.FELLOWSHIP);
        put(Phase.SHADOW, Keyword.SHADOW);
        put(Phase.MANEUVER, Keyword.MANEUVER);
        put(Phase.ARCHERY, Keyword.ARCHERY);
        put(Phase.ASSIGNMENT, Keyword.ASSIGNMENT);
        put(Phase.SKIRMISH, Keyword.SKIRMISH);
        put(Phase.REGROUP, Keyword.REGROUP);
    }});

    private static Filter getFullAttachValidTargetFilter(final DefaultGame game, final PhysicalCard card) {
        return Filters.and(RuleUtils.getFullValidTargetFilter(card.getOwner(), game, card),
                new Filter() {
                    @Override
                    public boolean accepts(DefaultGame game, PhysicalCard physicalCard) {
                        return game.getModifiersQuerying().canHavePlayedOn(game, card, physicalCard);
                    }
                }
        );
    }


    public static CostToEffectAction getPlayCardAction(DefaultGame game, PhysicalCard card, int twilightModifier,
                                                       Filterable additionalAttachmentFilter, boolean ignoreRoamingPenalty) {
        final LotroCardBlueprint blueprint = card.getBlueprint();

        if (blueprint.getCardType() != CardType.EVENT) {
            final Filterable validTargetFilter = blueprint.getValidTargetFilter(card.getOwner(), game, card);
            if (validTargetFilter == null) {
                PlayPermanentAction action = new PlayPermanentAction(card, getPlayToZone(card), twilightModifier, ignoreRoamingPenalty);

                game.getModifiersQuerying().appendExtraCosts(game, action, card);
                game.getModifiersQuerying().appendPotentialDiscounts(game, action, card);

                return action;
            } else {
                final AttachPermanentAction action = new AttachPermanentAction(game, card,
                        Filters.and(getFullAttachValidTargetFilter(game, card), additionalAttachmentFilter),
                        twilightModifier);

                game.getModifiersQuerying().appendPotentialDiscounts(game, action, card);
                game.getModifiersQuerying().appendExtraCosts(game, action, card);

                return action;
            }
        } else {
            final PlayEventAction action = blueprint.getPlayEventCardAction(card.getOwner(), game, card);

            game.getModifiersQuerying().appendPotentialDiscounts(game, action, card);
            game.getModifiersQuerying().appendExtraCosts(game, action, card);

            return action;
        }
    }

    public static boolean checkPlayRequirements(DefaultGame game, PhysicalCard card, Filterable additionalAttachmentFilter) {
        final LotroCardBlueprint blueprint = card.getBlueprint();

        // Check if card's own play requirements are met
        if (!card.getBlueprint().checkPlayRequirements(game, card))
            return false;

        // Check if there exists a legal target (if needed)
        final Filterable validTargetFilter = blueprint.getValidTargetFilter(card.getOwner(), game, card);
        Filterable finalTargetFilter = null;
        if (validTargetFilter != null) {
            finalTargetFilter = Filters.and(getFullAttachValidTargetFilter(game, card));
            if (Filters.countActive(game, finalTargetFilter) == 0)
                return false;
        }

        if (!game.getModifiersQuerying().canPlayCard(game, card.getOwner(), card))
            return false;

        // Return true if no other checks have failed
        return true;
    }
}
