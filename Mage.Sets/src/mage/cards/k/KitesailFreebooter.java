/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.cards.k;

import java.util.UUID;
import mage.MageInt;
import mage.MageObject;
import mage.abilities.Ability;
import mage.abilities.DelayedTriggeredAbility;
import mage.abilities.common.EntersBattlefieldTriggeredAbility;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.CreateDelayedTriggeredAbilityEffect;
import mage.abilities.keyword.FlyingAbility;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.Outcome;
import mage.constants.Zone;
import mage.filter.FilterCard;
import mage.filter.common.FilterNonlandCard;
import mage.filter.predicate.Predicates;
import mage.filter.predicate.mageobject.CardTypePredicate;
import mage.game.ExileZone;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.events.ZoneChangeEvent;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.TargetCard;
import mage.target.common.TargetOpponent;
import mage.util.CardUtil;

/**
 *
 * @author TheElk801
 */
public class KitesailFreebooter extends CardImpl {

    public KitesailFreebooter(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{1}{B}");

        this.subtype.add("Human");
        this.subtype.add("Pirate");
        this.power = new MageInt(1);
        this.toughness = new MageInt(2);

        // Flying
        this.addAbility(FlyingAbility.getInstance());

        // When Kitesail Freebooter enters the battlefield, target opponent reveals his or her hand. You choose a noncreature, nonland card from it. Exile that card until Kitesail Freebooter leaves the battlefield.
        Ability ability = new EntersBattlefieldTriggeredAbility(new KitesailFreebooterExileEffect());
        ability.addTarget(new TargetOpponent());
        ability.addEffect(new CreateDelayedTriggeredAbilityEffect(new KitesailFreebooterReturnExiledCardAbility()));
        this.addAbility(ability);
    }

    public KitesailFreebooter(final KitesailFreebooter card) {
        super(card);
    }

    @Override
    public KitesailFreebooter copy() {
        return new KitesailFreebooter(this);
    }
}

class KitesailFreebooterExileEffect extends OneShotEffect {

    public KitesailFreebooterExileEffect() {
        super(Outcome.Benefit);
        this.staticText = "target opponent reveals his or her hand. You choose a noncreature, nonland card from it. Exile that card until {this} leaves the battlefield";
    }

    public KitesailFreebooterExileEffect(final KitesailFreebooterExileEffect effect) {
        super(effect);
    }

    @Override
    public KitesailFreebooterExileEffect copy() {
        return new KitesailFreebooterExileEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        Player opponent = game.getPlayer(this.getTargetPointer().getFirst(game, source));
        Permanent sourcePermanent = game.getPermanentOrLKIBattlefield(source.getSourceId());
        if (controller != null && opponent != null && sourcePermanent != null) {
            if (!opponent.getHand().isEmpty()) {
                opponent.revealCards(sourcePermanent.getIdName(), opponent.getHand(), game);

                FilterCard filter = new FilterNonlandCard("noncreature, nonland card to exile");
                filter.add(Predicates.not(new CardTypePredicate(CardType.CREATURE)));
                TargetCard target = new TargetCard(Zone.HAND, filter);
                if (opponent.getHand().count(filter, game) > 0 && controller.choose(Outcome.Exile, opponent.getHand(), target, game)) {
                    Card card = opponent.getHand().get(target.getFirstTarget(), game);
                    // If source permanent leaves the battlefield before its triggered ability resolves, the target card won't be exiled.
                    if (card != null && game.getState().getZone(source.getSourceId()) == Zone.BATTLEFIELD) {
                        controller.moveCardToExileWithInfo(card, CardUtil.getExileZoneId(game, source.getSourceId(), source.getSourceObjectZoneChangeCounter()), sourcePermanent.getIdName(), source.getSourceId(), game, Zone.HAND, true);
                    }
                }
            }
            return true;
        }
        return false;

    }
}

/**
 * Returns the exiled card as source permanent leaves battlefield Uses no stack
 *
 * @author LevelX2
 */
class KitesailFreebooterReturnExiledCardAbility extends DelayedTriggeredAbility {

    public KitesailFreebooterReturnExiledCardAbility() {
        super(new KitesailFreebooterReturnExiledCardEffect(), Duration.OneUse);
        this.usesStack = false;
        this.setRuleVisible(false);
    }

    public KitesailFreebooterReturnExiledCardAbility(final KitesailFreebooterReturnExiledCardAbility ability) {
        super(ability);
    }

    @Override
    public KitesailFreebooterReturnExiledCardAbility copy() {
        return new KitesailFreebooterReturnExiledCardAbility(this);
    }

    @Override
    public boolean checkEventType(GameEvent event, Game game) {
        return event.getType() == GameEvent.EventType.ZONE_CHANGE;
    }

    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        if (event.getTargetId().equals(this.getSourceId())) {
            ZoneChangeEvent zEvent = (ZoneChangeEvent) event;
            if (zEvent.getFromZone() == Zone.BATTLEFIELD) {
                return true;
            }
        }
        return false;
    }
}

class KitesailFreebooterReturnExiledCardEffect extends OneShotEffect {

    public KitesailFreebooterReturnExiledCardEffect() {
        super(Outcome.Benefit);
        this.staticText = "Return exiled nonland card to its owner's hand";
    }

    public KitesailFreebooterReturnExiledCardEffect(final KitesailFreebooterReturnExiledCardEffect effect) {
        super(effect);
    }

    @Override
    public KitesailFreebooterReturnExiledCardEffect copy() {
        return new KitesailFreebooterReturnExiledCardEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        MageObject sourceObject = source.getSourceObject(game);
        if (sourceObject != null && controller != null) {
            ExileZone exile = game.getExile().getExileZone(CardUtil.getExileZoneId(game, source.getSourceId(), source.getSourceObjectZoneChangeCounter()));
            Permanent sourcePermanent = game.getPermanentOrLKIBattlefield(source.getSourceId());
            if (exile != null && sourcePermanent != null) {
                controller.moveCards(exile, Zone.HAND, source, game);
                return true;
            }
        }
        return false;
    }
}