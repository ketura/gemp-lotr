package com.gempukku.lotro.cards.set16.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Wraith Collection
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 4
 * Type: Minion • Wraith
 * Strength: 9
 * Vitality: 3
 * Site: 2
 * Game Text: Enduring. While you can spot another [WRAITH] Wraith, each wounded companion is resistance -2.
 * Shadow: Exert Undead of Angmar twice to play a [WRAITH] Wraith from your draw deck, then reshuffle.
 */
public class Card16_006 extends AbstractMinion {
    public Card16_006() {
        super(4, 9, 3, 2, Race.WRAITH, Culture.WRAITH, "Undead of Angmar", true);
        addKeyword(Keyword.ENDURING);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new ResistanceModifier(self, Filters.and(CardType.COMPANION, Filters.wounded), new SpotCondition(Filters.not(self), Culture.WRAITH, Race.WRAITH), -2);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSelfExert(self, 2, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(self));
            action.appendCost(
                    new SelfExertEffect(self));
            action.appendEffect(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Culture.WRAITH, Race.WRAITH));
            return Collections.singletonList(action);
        }
        return null;
    }
}
