package com.gempukku.lotro.cards;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.actions.Action;
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

    String getSubtitle();

    Signet getSignet();

    boolean hasKeyword(Keyword keyword);

    int getKeywordCount(Keyword keyword);

    Filterable getValidTargetFilter(String playerId, DefaultGame game, PhysicalCard self);

    int getTwilightCost();

    int getTwilightCostModifier(DefaultGame game, PhysicalCard self, PhysicalCard target);

    int getStrength();

    int getVitality();

    int getResistance();

    int[] getAllyHomeSiteNumbers();

    SitesBlock getAllyHomeSiteBlock();

    PlayEventAction getPlayEventCardAction(String playerId, DefaultGame game, PhysicalCard self);

    List<? extends Modifier> getInPlayModifiers(DefaultGame game, PhysicalCard self);

    List<? extends Modifier> getStackedOnModifiers(DefaultGame game, PhysicalCard self);

    List<? extends Modifier> getInDiscardModifiers(DefaultGame game, PhysicalCard self);

    List<? extends Modifier> getControlledSiteModifiers(DefaultGame game, PhysicalCard self);

    boolean checkPlayRequirements(DefaultGame game, PhysicalCard self);

    List<? extends Action> getPhaseActionsInHand(String playerId, DefaultGame game, PhysicalCard self);

    List<? extends Action> getPhaseActionsFromDiscard(String playerId, DefaultGame game, PhysicalCard self);

    List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, DefaultGame game, PhysicalCard self);

    List<? extends ActivateCardAction> getPhaseActionsFromStacked(String playerId, DefaultGame game, PhysicalCard self);

    List<RequiredTriggerAction> getRequiredBeforeTriggers(DefaultGame game, Effect effect, PhysicalCard self);

    List<RequiredTriggerAction> getRequiredAfterTriggers(DefaultGame game, EffectResult effectResult, PhysicalCard self);


    List<OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, DefaultGame game, Effect effect, PhysicalCard self);

    List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, DefaultGame game, EffectResult effectResult, PhysicalCard self);


    List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, DefaultGame game, Effect effect, PhysicalCard self);

    List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, DefaultGame game, EffectResult effectResult, PhysicalCard self);


    List<PlayEventAction> getPlayResponseEventAfterActions(String playerId, DefaultGame game, EffectResult effectResult, PhysicalCard self);

    List<PlayEventAction> getPlayResponseEventBeforeActions(String playerId, DefaultGame game, Effect effect, PhysicalCard self);


    List<OptionalTriggerAction> getOptionalInHandAfterTriggers(String playerId, DefaultGame game, EffectResult effectResult, PhysicalCard self);


    RequiredTriggerAction getDiscardedFromPlayRequiredTrigger(DefaultGame game, PhysicalCard self);

    OptionalTriggerAction getDiscardedFromPlayOptionalTrigger(String playerId, DefaultGame game, PhysicalCard self);


    RequiredTriggerAction getKilledRequiredTrigger(DefaultGame game, PhysicalCard self);

    OptionalTriggerAction getKilledOptionalTrigger(String playerId, DefaultGame game, PhysicalCard self);

    SitesBlock getSiteBlock();

    int getSiteNumber();

    Set<PossessionClass> getPossessionClasses();

    boolean isExtraPossessionClass(DefaultGame game, PhysicalCard self, PhysicalCard attachedTo);

    Direction getSiteDirection();

    String getDisplayableInformation(PhysicalCard self);

    List<? extends ExtraPlayCost> getExtraCostToPlay(DefaultGame game, PhysicalCard self);

    int getPotentialDiscount(DefaultGame game, String playerId, PhysicalCard self);

    void appendPotentialDiscountEffects(DefaultGame game, CostToEffectAction action, String playerId, PhysicalCard self);

    boolean canPayAidCost(DefaultGame game, PhysicalCard self);

    void appendAidCosts(DefaultGame game, CostToEffectAction action, PhysicalCard self);

    boolean skipUniquenessCheck();
}
