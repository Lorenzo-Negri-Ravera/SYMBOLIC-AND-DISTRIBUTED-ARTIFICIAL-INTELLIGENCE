package it.unige.cleaningrobots;

import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import org.eclipse.xtext.xbase.lib.XbaseGenerated;

@SarlSpecification("0.15")
@SarlElementType(10)
@XbaseGenerated
@SuppressWarnings("all")
public class GlobalConfig {
  public static final int GRID_SIZE = 20;

  public static final int NUM_AGENTS = 3;

  public static final int MAX_DIRT = 15;

  @SyntheticMember
  public GlobalConfig() {
    super();
  }
}
