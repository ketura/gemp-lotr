package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.TakeControlOfASiteEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.modifiers.ShouldSkipPhaseModifier;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 6
 * Clan of the Hills
 * Minion • Man
 * 12	1	3
 * While this minion is in region 2, skip the archery phase.
 * Each time this minion wins a skirmish, you may discard another [Dunland] Man to take control of a site.
 * http://lotrtcg.org/coreset/dunland/clanofthehills(r1).png
 */
public class Card20_014 extends AbstractMinion {
    public Card20_014() {
        super(6, 12, 1, 3, Race.MAN, Culture.DUNLAND, "Clan of the Hills");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new ShouldSkipPhaseModifier(self, new LocationCondition(Filters.region(2)), Phase.ARCHERY));
        return modifiers;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self)
                && PlayConditions.canDiscardFromPlay(self, game, Filters.not(self), Culture.DUNLAND, Race.MAN)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.not(self), Culture.DUNLAND, Race.MAN));
            action.appendEffect(
                    new TakeControlOfASiteEffect(self, playerId));
            return Collections.singletonList(action);
        }
        return null;
    }
}
