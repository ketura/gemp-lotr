package com.gempukku.lotro.cards;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.actions.Action;
import com.gempukku.lotro.game.actions.ActivateCardAction;
import com.gempukku.lotro.game.actions.OptionalTriggerAction;
import com.gempukku.lotro.game.actions.lotronly.*;
import com.gempukku.lotro.game.modifiers.ExtraPlayCost;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.Modifier;
import com.gempukku.lotro.game.effects.Effect;
import com.gempukku.lotro.game.effects.EffectResult;

import java.util.List;
import java.util.Set;

public interface LotroCardBlueprint {
    enum Direction {
        LEFT, RIGHT
    }

    Side getSide();

    CardType getCardType();

    Culture getCulture();

    Race getRace();

    boolean isUnique();

    String getTitle();
    String getImageUrl();

    String getSubtitle();

    Signet getSignet();

    boolean hasKeyword(Keyword keyword);

    int getKeywordCount(Keyword keyword);

    Filterable getValidTargetFilter(String playerId, DefaultGame game, LotroPhysicalCard self);

    int getTwilightCost();

    int getTwilightCostModifier(DefaultGame game, LotroPhysicalCard self, LotroPhysicalCard target);

    int getStrength();

    int getVitality();

    int getResistance();

    int[] getAllyHomeSiteNumbers();
    int getTribbleValue();
    String getTribblePower();

    SitesBlock getAllyHomeSiteBlock();

    PlayEventAction getPlayEventCardAction(String playerId, DefaultGame game, LotroPhysicalCard self);

    List<? extends Modifier> getInPlayModifiers(DefaultGame game, LotroPhysicalCard self);

    List<? extends Modifier> getStackedOnModifiers(DefaultGame game, LotroPhysicalCard self);

    List<? extends Modifier> getInDiscardModifiers(DefaultGame game, LotroPhysicalCard self);

    List<? extends Modifier> getControlledSiteModifiers(DefaultGame game, LotroPhysicalCard self);

    boolean checkPlayRequirements(DefaultGame game, LotroPhysicalCard self);

    List<? extends Action> getPhaseActionsInHand(String playerId, DefaultGame game, LotroPhysicalCard self);

    List<? extends Action> getPhaseActionsFromDiscard(String playerId, DefaultGame game, LotroPhysicalCard self);

    List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, DefaultGame game, LotroPhysicalCard self);

    List<? extends ActivateCardAction> getPhaseActionsFromStacked(String playerId, DefaultGame game, LotroPhysicalCard self);

    List<RequiredTriggerAction> getRequiredBeforeTriggers(DefaultGame game, Effect effect, LotroPhysicalCard self);

    List<RequiredTriggerAction> getRequiredAfterTriggers(DefaultGame game, EffectResult effectResult, LotroPhysicalCard self);


    List<OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, DefaultGame game, Effect effect, LotroPhysicalCard self);

    List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, DefaultGame game, EffectResult effectResult, LotroPhysicalCard self);


    List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, DefaultGame game, Effect effect, LotroPhysicalCard self);

    List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, DefaultGame game, EffectResult effectResult, LotroPhysicalCard self);


    List<PlayEventAction> getPlayResponseEventAfterActions(String playerId, DefaultGame game, EffectResult effectResult, LotroPhysicalCard self);

    List<PlayEventAction> getPlayResponseEventBeforeActions(String playerId, DefaultGame game, Effect effect, LotroPhysicalCard self);


    List<OptionalTriggerAction> getOptionalInHandAfterTriggers(String playerId, DefaultGame game, EffectResult effectResult, LotroPhysicalCard self);


    RequiredTriggerAction getDiscardedFromPlayRequiredTrigger(DefaultGame game, LotroPhysicalCard self);

    OptionalTriggerAction getDiscardedFromPlayOptionalTrigger(String playerId, DefaultGame game, LotroPhysicalCard self);


    RequiredTriggerAction getKilledRequiredTrigger(DefaultGame game, LotroPhysicalCard self);

    OptionalTriggerAction getKilledOptionalTrigger(String playerId, DefaultGame game, LotroPhysicalCard self);

    SitesBlock getSiteBlock();

    int getSiteNumber();

    Set<PossessionClass> getPossessionClasses();

    boolean isExtraPossessionClass(DefaultGame game, LotroPhysicalCard self, LotroPhysicalCard attachedTo);

    Direction getSiteDirection();

    String getDisplayableInformation(LotroPhysicalCard self);

    List<? extends ExtraPlayCost> getExtraCostToPlay(DefaultGame game, LotroPhysicalCard self);

    int getPotentialDiscount(DefaultGame game, String playerId, LotroPhysicalCard self);

    void appendPotentialDiscountEffects(DefaultGame game, CostToEffectAction action, String playerId, LotroPhysicalCard self);

    boolean canPayAidCost(DefaultGame game, LotroPhysicalCard self);

    void appendAidCosts(DefaultGame game, CostToEffectAction action, LotroPhysicalCard self);

    boolean skipUniquenessCheck();
}
