package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.RemoveKeywordModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Brego, Kingly Mount
 * Rohan	Possession •  Mount
 * To play spot a [Rohan] man.
 * Bearer must be a Man, Elf, or Wizard.
 * At the start of each skirmish involving bearer, each unmounted minion skirmishing bearer must exert.
 * Each minion assigned to bearer loses Lurker.
 */
public class Card20_314 extends AbstractAttachableFPPossession {
    public Card20_314() {
        super(1, 0, 0, Culture.ROHAN, PossessionClass.MOUNT, "Brego", "Kingly Mount", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.or(Race.MAN, Race.ELF, Race.WIZARD);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, Filter additionalAttachmentFilter, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, additionalAttachmentFilter, twilightModifier)
                && PlayConditions.canSpot(game, Culture.ROHAN, Race.MAN);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.SKIRMISH)
                && PlayConditions.isActive(game, Filters.hasAttached(self), Filters.inSkirmish)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ExertCharactersEffect(action, self, CardType.MINION, Filters.not(Filters.mounted), Filters.inSkirmish));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new RemoveKeywordModifier(self, Filters.and(CardType.MINION, Filters.assignedAgainst(Filters.hasAttached(self))), Keyword.LURKER));
    }
}
