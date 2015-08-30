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
package mage.sets.battleforzendikar;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.DiesThisOrAnotherCreatureTriggeredAbility;
import mage.abilities.common.LandfallAbility;
import mage.abilities.effects.common.CreateTokenEffect;
import mage.abilities.effects.common.DamageTargetEffect;
import mage.cards.CardImpl;
import mage.constants.CardType;
import mage.constants.Rarity;
import mage.filter.common.FilterCreaturePermanent;
import mage.filter.predicate.mageobject.SubtypePredicate;
import mage.game.permanent.token.Token;
import mage.target.common.TargetCreatureOrPlayer;

/**
 *
 * @author fireshoes
 */
public class OmnathLocusOfRage extends CardImpl {
    
    private static final FilterCreaturePermanent filter = new FilterCreaturePermanent("{this} or another Elemental");
    
    static {
        filter.add(new SubtypePredicate("Elemental"));
    }

    public OmnathLocusOfRage(UUID ownerId) {
        super(ownerId, 217, "Omnath, Locus of Rage", Rarity.MYTHIC, new CardType[]{CardType.CREATURE}, "{3}{R}{R}{G}{G}");
        this.expansionSetCode = "BFZ";
        this.supertype.add("Legendary");
        this.subtype.add("Elemental");
        this.power = new MageInt(5);
        this.toughness = new MageInt(5);

        // <i>Landfall</i> - Whenever a land enters the battlefield under your control, put a 5/5 red and green Elemental creature token onto the battlefield.
        this.addAbility(new LandfallAbility(new CreateTokenEffect(new OmnathElementalToken()), false));
        
        // Whenever Omnath, Locus of Rage or another Elemental you control dies, Omnath deals 3 damage to target creature or player.
        Ability ability = new DiesThisOrAnotherCreatureTriggeredAbility(new DamageTargetEffect(3), false, filter);
        ability.addTarget(new TargetCreatureOrPlayer());
        this.addAbility(ability);
    }

    public OmnathLocusOfRage(final OmnathLocusOfRage card) {
        super(card);
    }

    @Override
    public OmnathLocusOfRage copy() {
        return new OmnathLocusOfRage(this);
    }
}

class OmnathElementalToken extends Token {

    OmnathElementalToken() {
        super("Elemental", "5/5 red and green Elemental creature token");
        setTokenType(1);
        setOriginalExpansionSetCode("BFZ");
        cardType.add(CardType.CREATURE);
        subtype.add("Elemental");

        color.setRed(true);
        color.setGreen(true);
        power = new MageInt(5);
        toughness = new MageInt(5);
    }
}