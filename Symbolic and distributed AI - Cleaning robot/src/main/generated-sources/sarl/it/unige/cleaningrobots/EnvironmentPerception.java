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
public class EnvironmentPerception extends Event {
  public int x;

  public int y;

  public boolean hasDirt;

  public EnvironmentPerception(final int x, final int y, final boolean hasDirt) {
    this.x = x;
    this.y = y;
    this.hasDirt = hasDirt;
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
    EnvironmentPerception other = (EnvironmentPerception) obj;
    if (other.x != this.x)
      return false;
    if (other.y != this.y)
      return false;
    if (other.hasDirt != this.hasDirt)
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
    result = prime * result + Boolean.hashCode(this.hasDirt);
    return result;
  }

  /**
   * Returns a String representation of the EnvironmentPerception event's attributes only.
   */
  @SyntheticMember
  @Pure
  protected void toString(final ToStringBuilder builder) {
    super.toString(builder);
    builder.add("x", this.x);
    builder.add("y", this.y);
    builder.add("hasDirt", this.hasDirt);
  }

  @SyntheticMember
  private static final long serialVersionUID = 2273238357L;
}
