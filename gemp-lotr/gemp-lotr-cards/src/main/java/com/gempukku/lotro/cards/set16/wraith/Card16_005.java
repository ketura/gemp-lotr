package com.gempukku.lotro.cards.set16.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.modifiers.CantHealModifier;
import com.gempukku.lotro.cards.modifiers.CantRemoveBurdensModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Wraith Collection
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 7
 * Type: Minion • Wraith
 * Strength: 14
 * Vitality: 3
 * Site: 2
 * Game Text: Enduring. While you can spot another [WRAITH] Wraith, wounds cannot be healed and burdens can
 * not be removed (except by Wraiths). Skirmish: Exert Spirit of Dread to make another [WRAITH] Wraith strength +1.
 */
public class Card16_005 extends AbstractMinion {
    public Card16_005() {
        super(7, 14, 3, 2, Race.WRAITH, Culture.WRAITH, "Spirit of Dread", null, true);
        addKeyword(Keyword.ENDURING);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new CantHealModifier(self, new SpotCondition(Filters.not(self), Culture.WRAITH, Race.WRAITH), Filters.any));
        modifiers.add(
                new CantRemoveBurdensModifier(self, new SpotCondition(Filters.not(self), Culture.WRAITH, Race.WRAITH), Filters.not(Race.WRAITH)));
        return modifiers;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(
                            action, self, playerId, 1, Filters.not(self), Culture.WRAITH, Race.WRAITH));
            return Collections.singletonList(action);
        }
        return null;
    }
}
