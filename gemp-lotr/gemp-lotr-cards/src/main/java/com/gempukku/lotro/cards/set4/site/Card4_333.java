package com.gempukku.lotro.cards.set4.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Twilight Cost: 1
 * Type: Site
 * Site: 2T
 * Game Text: Plains. At the start of each fellowship phase, heal up to 3 wounds from companions.
 */
public class Card4_333 extends AbstractSite {
    public Card4_333() {
        super("Plains of Rohan Camp", SitesBlock.TWO_TOWERS, 2, 1, Direction.LEFT);
        addKeyword(Keyword.PLAINS);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.FELLOWSHIP)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 0, 1, CardType.COMPANION));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 0, 1, CardType.COMPANION));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 0, 1, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
