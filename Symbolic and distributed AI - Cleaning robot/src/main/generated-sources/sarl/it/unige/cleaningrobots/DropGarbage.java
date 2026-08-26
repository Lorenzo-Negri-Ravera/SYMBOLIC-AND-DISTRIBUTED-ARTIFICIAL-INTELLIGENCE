package it.unige.cleaningrobots;

import io.sarl.lang.core.Address;
import io.sarl.lang.core.Event;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import org.eclipse.xtext.xbase.lib.XbaseGenerated;

@SarlSpecification("0.15")
@SarlElementType(15)
@XbaseGenerated
@SuppressWarnings("all")
public class DropGarbage extends Event {
  @SyntheticMember
  public DropGarbage() {
    super();
  }

  @SyntheticMember
  public DropGarbage(final Address source) {
    super(source);
  }

  @SyntheticMember
  private static final long serialVersionUID = 588368462L;
}
