package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.modifiers.PlayersCantPlayPhaseEventsOrPhaseSpecialAbilitiesModifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Title: Tower of Barad-dur
 * Set: Second Edition
 * Side: None
 * Site Number: 9
 * Shadow Number: 10
 * Card Number: 1U309
 * Game Text: Shadow: Play The Great Eye from your draw deck or discard pile; it's twilight cost is -3; end your Shadow phase.
 */
public class Card40_309 extends AbstractSite {
    public Card40_309() {
        super("Tower of Barad-dûr", SitesBlock.SECOND_ED, 9, 10, Direction.LEFT);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SHADOW, self)
                && GameUtils.isShadow(game, playerId)
                && (PlayConditions.canPlayFromDeck(playerId, game, Filters.name("The Great Eye"))
                || PlayConditions.canPlayFromDiscard(playerId, game, -3, Filters.name("The Great Eye")))) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleEffects = new ArrayList<Effect>(2);
            possibleEffects.add(
                    new ChooseAndPlayCardFromDeckEffect(playerId, -3, Filters.name("The Great Eye")));
            possibleEffects.add(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, -3, Filters.name("The Great Eye")));
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new PlayersCantPlayPhaseEventsOrPhaseSpecialAbilitiesModifier(null, Phase.SHADOW)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
