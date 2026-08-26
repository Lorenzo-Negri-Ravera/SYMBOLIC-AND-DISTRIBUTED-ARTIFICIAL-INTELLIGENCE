package it.unige.cleaningrobots;

import io.sarl.api.core.AgentTask;
import io.sarl.api.core.DefaultContextInteractions;
import io.sarl.api.core.Initialize;
import io.sarl.api.core.Logging;
import io.sarl.api.core.Schedules;
import io.sarl.lang.core.Agent;
import io.sarl.lang.core.AtomicSkillReference;
import io.sarl.lang.core.DynamicSkillProvider;
import io.sarl.lang.core.Event;
import io.sarl.lang.core.annotation.ImportedCapacityFeature;
import io.sarl.lang.core.annotation.PerceptGuardEvaluator;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import io.sarl.lang.core.scoping.extensions.cast.PrimitiveCastExtensions;
import jakarta.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.XbaseGenerated;

@SarlSpecification("0.15")
@SarlElementType(19)
@XbaseGenerated
@SuppressWarnings("all")
public class ExplorerAgentt extends Agent {
  private int INCINERATOR_posX = (GlobalConfig.GRID_SIZE / 2);

  private int INCINERATOR_posY = (GlobalConfig.GRID_SIZE / 2);

  private int my_x = 0;

  private int my_y = 0;

  private int return_x = 0;

  private int return_y = 0;

  private Random random = new Random();

  private String state = "PATROLLING";

  private void $behaviorUnit$Initialize$0(final Initialize occurrence) {
    int _size = ((List<Object>)Conversions.doWrapArray(occurrence.parameters)).size();
    if ((_size >= 2)) {
      Object _get = occurrence.parameters[0];
      this.my_x = (((_get == null ? null : PrimitiveCastExtensions.toInteger(_get))) == null ? 0 : ((_get == null ? null : PrimitiveCastExtensions.toInteger(_get))).intValue());
      Object _get_1 = occurrence.parameters[1];
      this.my_y = (((_get_1 == null ? null : PrimitiveCastExtensions.toInteger(_get_1))) == null ? 0 : ((_get_1 == null ? null : PrimitiveCastExtensions.toInteger(_get_1))).intValue());
    }
    Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.info((((("SPWN: Robot spawned at: " + Integer.valueOf(this.my_x)) + ",") + Integer.valueOf(this.my_y)) + ". Starting patrol."));
    DefaultContextInteractions _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
    NextCellMovement _nextCellMovement = new NextCellMovement(this.my_x, this.my_y);
    _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_nextCellMovement);
  }

  private void $behaviorUnit$EnvironmentPerception$1(final EnvironmentPerception occurrence) {
    this.my_x = occurrence.x;
    this.my_y = occurrence.y;
    boolean _equals = Objects.equals(this.state, "PATROLLING");
    if (_equals) {
      if (occurrence.hasDirt) {
        Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
        _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.info((((("FOUND: Garbage at " + Integer.valueOf(this.my_x)) + ",") + Integer.valueOf(this.my_y)) + ". Picking it up and heading to the incinerator."));
        DefaultContextInteractions _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
        PickGarbage _pickGarbage = new PickGarbage(this.my_x, this.my_y);
        _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_pickGarbage);
        this.return_x = this.my_x;
        this.return_y = this.my_y;
        this.state = "TO_INCINERATOR";
        this.moveToward(this.INCINERATOR_posX, this.INCINERATOR_posY);
      } else {
        this.moveRandomly();
      }
    } else {
      boolean _equals_1 = Objects.equals(this.state, "TO_INCINERATOR");
      if (_equals_1) {
        if (((this.my_x == this.INCINERATOR_posX) && (this.my_y == this.INCINERATOR_posY))) {
          Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_1 = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
          _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_1.info("ARRIVE: Reached the incinerator. Dropping the garbage!");
          DefaultContextInteractions _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER_1 = this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
          DropGarbage _dropGarbage = new DropGarbage();
          _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER_1.emit(_dropGarbage);
          this.state = "RETURNING";
          this.moveToward(this.return_x, this.return_y);
        } else {
          this.moveToward(this.INCINERATOR_posX, this.INCINERATOR_posY);
        }
      } else {
        boolean _equals_2 = Objects.equals(this.state, "RETURNING");
        if (_equals_2) {
          if (((this.my_x == this.return_x) && (this.my_y == this.return_y))) {
            Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_2 = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
            _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_2.info("RETURN: Back at the interruption point. Resuming patrol.");
            this.state = "PATROLLING";
            this.moveRandomly();
          } else {
            this.moveToward(this.return_x, this.return_y);
          }
        }
      }
    }
  }

  protected AgentTask moveToward(final int target_x, final int target_y) {
    AgentTask _xblockexpression = null;
    {
      int next_x = this.my_x;
      int next_y = this.my_y;
      if ((this.my_x < target_x)) {
        next_x++;
      } else {
        if ((this.my_x > target_x)) {
          next_x--;
        }
      }
      if ((this.my_y < target_y)) {
        next_y++;
      } else {
        if ((this.my_y > target_y)) {
          next_y--;
        }
      }
      final int final_x = next_x;
      final int final_y = next_y;
      Schedules _$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER();
      final Procedure1<Agent> _function = (Agent it) -> {
        DefaultContextInteractions _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
        NextCellMovement _nextCellMovement = new NextCellMovement(final_x, final_y);
        _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_nextCellMovement);
      };
      _xblockexpression = _$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER.in(500, _function);
    }
    return _xblockexpression;
  }

  protected AgentTask moveRandomly() {
    AgentTask _xblockexpression = null;
    {
      int _nextInt = this.random.nextInt(3);
      int dx = (_nextInt - 1);
      int _nextInt_1 = this.random.nextInt(3);
      int dy = (_nextInt_1 - 1);
      int next_x = (this.my_x + dx);
      int next_y = (this.my_y + dy);
      if ((next_x < 0)) {
        next_x = 0;
      }
      if ((next_x >= GlobalConfig.GRID_SIZE)) {
        next_x = (GlobalConfig.GRID_SIZE - 1);
      }
      if ((next_y < 0)) {
        next_y = 0;
      }
      if ((next_y >= GlobalConfig.GRID_SIZE)) {
        next_y = (GlobalConfig.GRID_SIZE - 1);
      }
      final int final_x = next_x;
      final int final_y = next_y;
      Schedules _$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER();
      final Procedure1<Agent> _function = (Agent it) -> {
        DefaultContextInteractions _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
        NextCellMovement _nextCellMovement = new NextCellMovement(final_x, final_y);
        _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_nextCellMovement);
      };
      _xblockexpression = _$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER.in(500, _function);
    }
    return _xblockexpression;
  }

  @Extension
  @ImportedCapacityFeature(Logging.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_API_CORE_LOGGING;

  @SyntheticMember
  @Pure
  private Logging $CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING == null || this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING.get() == null) {
      this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING = $getSkill(Logging.class);
    }
    return $castSkill(Logging.class, this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING);
  }

  @Extension
  @ImportedCapacityFeature(DefaultContextInteractions.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS;

  @SyntheticMember
  @Pure
  private DefaultContextInteractions $CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS == null || this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS.get() == null) {
      this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS = $getSkill(DefaultContextInteractions.class);
    }
    return $castSkill(DefaultContextInteractions.class, this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS);
  }

  @Extension
  @ImportedCapacityFeature(Schedules.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES;

  @SyntheticMember
  @Pure
  private Schedules $CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES == null || this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES.get() == null) {
      this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES = $getSkill(Schedules.class);
    }
    return $castSkill(Schedules.class, this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES);
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Initialize(final Initialize occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Initialize$0(occurrence));
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$EnvironmentPerception(final EnvironmentPerception occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$EnvironmentPerception$1(occurrence));
  }

  @SyntheticMember
  @Override
  public void $getSupportedEvents(final Set<Class<? extends Event>> toBeFilled) {
    super.$getSupportedEvents(toBeFilled);
    toBeFilled.add(Initialize.class);
    toBeFilled.add(EnvironmentPerception.class);
  }

  @SyntheticMember
  @Override
  public boolean $isSupportedEvent(final Class<? extends Event> event) {
    if (Initialize.class.isAssignableFrom(event)) {
      return true;
    }
    if (EnvironmentPerception.class.isAssignableFrom(event)) {
      return true;
    }
    return false;
  }

  @SyntheticMember
  @Override
  public void $evaluateBehaviorGuards(final Class<?> eventType, final Object event, final Collection<Runnable> callbacks) {
    assert eventType != null;
    assert event != null;
    super.$evaluateBehaviorGuards(eventType, event, callbacks);
    if (Initialize.class.equals(eventType)) {
      final var occurrence = (Initialize) event;
      $guardEvaluator$Initialize(occurrence, callbacks);
    }
    if (EnvironmentPerception.class.equals(eventType)) {
      final var occurrence = (EnvironmentPerception) event;
      $guardEvaluator$EnvironmentPerception(occurrence, callbacks);
    }
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
    ExplorerAgentt other = (ExplorerAgentt) obj;
    if (other.INCINERATOR_posX != this.INCINERATOR_posX)
      return false;
    if (other.INCINERATOR_posY != this.INCINERATOR_posY)
      return false;
    if (other.my_x != this.my_x)
      return false;
    if (other.my_y != this.my_y)
      return false;
    if (other.return_x != this.return_x)
      return false;
    if (other.return_y != this.return_y)
      return false;
    if (!Objects.equals(this.state, other.state))
      return false;
    return super.equals(obj);
  }

  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Integer.hashCode(this.INCINERATOR_posX);
    result = prime * result + Integer.hashCode(this.INCINERATOR_posY);
    result = prime * result + Integer.hashCode(this.my_x);
    result = prime * result + Integer.hashCode(this.my_y);
    result = prime * result + Integer.hashCode(this.return_x);
    result = prime * result + Integer.hashCode(this.return_y);
    result = prime * result + Objects.hashCode(this.state);
    return result;
  }

  @SyntheticMember
  public ExplorerAgentt(final UUID parentID, final UUID agentID) {
    super(parentID, agentID);
  }

  @SyntheticMember
  @Inject
  public ExplorerAgentt(final UUID parentID, final UUID agentID, final DynamicSkillProvider skillProvider) {
    super(parentID, agentID, skillProvider);
  }
}
