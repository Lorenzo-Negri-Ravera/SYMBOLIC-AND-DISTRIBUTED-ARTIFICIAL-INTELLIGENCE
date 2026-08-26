package it.unige.cleaningrobots;

import io.sarl.lang.core.Event;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.XbaseGenerated;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@SarlSpecification("0.15")
@SarlElementType(15)
@XbaseGenerated
@SuppressWarnings("all")
public class PickGarbage extends Event {
  public int x;

  public int y;

  public PickGarbage(final int x, final int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  @Pure
  @SyntheticMember
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PickGarbage other = (PickGarbage) obj;
    if (other.x != this.x)
      return false;
    if (other.y != this.y)
      return false;
    return super.equals(obj);
  }

  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Integer.hashCode(this.x);
    result = prime * result + Integer.hashCode(this.y);
    return result;
  }

  /**
   * Returns a String representation of the PickGarbage event's attributes only.
   */
  @SyntheticMember
  @Pure
  protected void toString(final ToStringBuilder builder) {
    super.toString(builder);
    builder.add("x", this.x);
    builder.add("y", this.y);
  }

  @SyntheticMember
  private static final long serialVersionUID = 692205106L;
}
