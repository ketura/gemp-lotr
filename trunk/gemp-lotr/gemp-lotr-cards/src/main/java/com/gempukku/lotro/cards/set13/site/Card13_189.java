package com.gempukku.lotro.cards.set13.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Set: Bloodlines
 * Twilight Cost: 3
 * Type: Site
 * Game Text: Forest. When the fellowship moves to this site during the regroup phase, the Free Peoples player may heal
 * a companion of each culture.
 */
public class Card13_189 extends AbstractNewSite {
    public Card13_189() {
        super("Crossroads of the Fallen Kings", 3, Direction.RIGHT);
        addKeyword(Keyword.FOREST);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)
                && PlayConditions.isPhase(game, Phase.REGROUP)
                && playerId.equals(game.getGameState().getCurrentPlayerId())) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            Set<Culture> companionCultures = new HashSet<Culture>();
            for (PhysicalCard companion : Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION)) {
                if (companion.getBlueprint().getCulture() != null)
                    companionCultures.add(companion.getBlueprint().getCulture());
            }

            final Set<PhysicalCard> companionsToHeal = new HashSet<PhysicalCard>();

            for (Culture companionCulture : companionCultures) {
                action.appendEffect(
                        new ChooseActiveCardEffect(self, playerId, "Choose a companion to heal", CardType.COMPANION, companionCulture, Filters.canHeal) {
                            @Override
                            protected void cardSelected(LotroGame game, PhysicalCard card) {
                                companionsToHeal.add(card);
                            }
                        });
            }

            action.appendEffect(
                    new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            action.appendEffect(
                                    new HealCharactersEffect(self, Filters.in(companionsToHeal)));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
