package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.Preventable;

public class TakeControlOfASiteEffect extends AbstractEffect implements Preventable {
    private PhysicalCard _source;
    private String _playerId;

    private boolean _prevented;

    public TakeControlOfASiteEffect(PhysicalCard source, String playerId) {
        _source = source;
        _playerId = playerId;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return getFirstControllableSite(game) != null;
    }

    private PhysicalCard getFirstControllableSite(LotroGame game) {
        int maxUnoccupiedSite = Integer.MAX_VALUE;
        for (String playerId : game.getGameState().getPlayerOrder().getAllPlayers())
            maxUnoccupiedSite = Math.min(maxUnoccupiedSite, game.getGameState().getPlayerPosition(playerId) - 1);

        for (int i = 1; i <= maxUnoccupiedSite; i++) {
            final PhysicalCard site = game.getGameState().getSite(i);
            if (site.getCardController() == null)
                return site;
        }

        return null;
    }

    @Override
    public String getText(LotroGame game) {
        return "Take control of a site";
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.TAKE_CONTROL_OF_A_SITE;
    }

    @Override
    public void prevent() {
        _prevented = true;
    }

    @Override
    public boolean isPrevented() {
        return _prevented;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        PhysicalCard site = getFirstControllableSite(game);
        if (site != null && !_prevented) {
            game.getGameState().takeControlOfCard(_playerId, site, Zone.SUPPORT);
            game.getGameState().sendMessage(_playerId + " took control of " + GameUtils.getCardLink(site));
            return new FullEffectResult(null, true, true);
        }
        return new FullEffectResult(null, false, false);
    }
}
