package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.rules.GameUtils;
import com.gempukku.lotro.game.Preventable;
import com.gempukku.lotro.effects.results.TakeControlOfSiteResult;

public class TakeControlOfASiteEffect extends AbstractEffect implements Preventable {
    private final LotroPhysicalCard _source;
    private final String _playerId;

    private boolean _prevented;

    public TakeControlOfASiteEffect(LotroPhysicalCard source, String playerId) {
        _source = source;
        _playerId = playerId;
    }

    public LotroPhysicalCard getSource() {
        return _source;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return getFirstControllableSite(game) != null;
    }

    private LotroPhysicalCard getFirstControllableSite(DefaultGame game) {
        int maxUnoccupiedSite = Integer.MAX_VALUE;
        for (String playerId : game.getGameState().getPlayerOrder().getAllPlayers())
            maxUnoccupiedSite = Math.min(maxUnoccupiedSite, game.getGameState().getPlayerPosition(playerId) - 1);

        for (int i = 1; i <= maxUnoccupiedSite; i++) {
            final LotroPhysicalCard site = game.getGameState().getSite(i);
            if (site.getCardController() == null)
                return site;
        }

        return null;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Take control of a site";
    }

    @Override
    public Effect.Type getType() {
        return Type.BEFORE_TAKE_CONTROL_OF_A_SITE;
    }

    @Override
    public void prevent() {
        _prevented = true;
    }

    @Override
    public boolean isPrevented(DefaultGame game) {
        return _prevented;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        LotroPhysicalCard site = getFirstControllableSite(game);
        if (site != null && !_prevented) {
            game.getGameState().takeControlOfCard(_playerId, game, site, Zone.SUPPORT);
            game.getGameState().sendMessage(_playerId + " took control of " + GameUtils.getCardLink(site));
            game.getActionsEnvironment().emitEffectResult(new TakeControlOfSiteResult(_playerId));
            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }
}
