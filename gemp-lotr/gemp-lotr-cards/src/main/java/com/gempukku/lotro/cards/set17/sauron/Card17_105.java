package com.gempukku.lotro.cards.set17.sauron;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.modifiers.*;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Artifact • Support Area
 * Game Text: To play, spot 2 minions. While you can spot Sauron, each other minion is damage +1. While the fellowship
 * is in the same region as Mount Doom, each companion is resistance -2.
 */
public class Card17_105 extends AbstractPermanent {
    public Card17_105() {
        super(Side.SHADOW, 2, CardType.ARTIFACT, Culture.SAURON, "Throne of the Dark Lord", null, true);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 2, CardType.MINION);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, Filters.and(CardType.MINION, Filters.not(Filters.name("Sauron"))),
                        new SpotCondition(Filters.name("Sauron")), Keyword.DAMAGE, 1));
        modifiers.add(
                new ResistanceModifier(self, CardType.COMPANION,
                        new Condition() {
                            @Override
                            public boolean isFullfilled(LotroGame game) {
                                for (int siteNo = 1; siteNo <= 9; siteNo++) {
                                    if (GameUtils.getRegion(siteNo) == GameUtils.getRegion(game)) {
                                        PhysicalCard site = game.getGameState().getSite(siteNo);
                                        if (site != null && site.getBlueprint().getTitle().equals("Mount Doom"))
                                            return true;

                                    }
                                }
                                return false;
                            }
                        }, -2));
        return modifiers;
    }
}
