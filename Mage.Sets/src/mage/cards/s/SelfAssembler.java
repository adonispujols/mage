
package mage.cards.s;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.common.EntersBattlefieldTriggeredAbility;
import mage.abilities.effects.Effect;
import mage.abilities.effects.common.search.SearchLibraryPutInHandEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.filter.FilterCard;
import mage.filter.predicate.mageobject.SubtypePredicate;
import mage.target.common.TargetCardInLibrary;

/**
 *
 * @author fireshoes
 */
public final class SelfAssembler extends CardImpl {

    private static final FilterCard filter = new FilterCard("an Assembly-Worker card");

    static {
        filter.add(new SubtypePredicate(SubType.ASSEMBLY_WORKER));
    }

    public SelfAssembler(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.ARTIFACT,CardType.CREATURE},"{5}");
        this.subtype.add(SubType.ASSEMBLY_WORKER);
        this.power = new MageInt(4);
        this.toughness = new MageInt(4);

        // When Self-Assembler enters the battlefield, you may search your library for an Assembly-Worker creature card, reveal it, put it into your hand,
        // then shuffle your library.
        Effect effect = new SearchLibraryPutInHandEffect(new TargetCardInLibrary(0, 1, filter), true, true);
        effect.setText("you may search your library for an Assembly-Worker card, reveal it, put it into your hand, then shuffle your library");
        this.addAbility(new EntersBattlefieldTriggeredAbility(effect, true));
    }

    public SelfAssembler(final SelfAssembler card) {
        super(card);
    }

    @Override
    public SelfAssembler copy() {
        return new SelfAssembler(this);
    }
}
