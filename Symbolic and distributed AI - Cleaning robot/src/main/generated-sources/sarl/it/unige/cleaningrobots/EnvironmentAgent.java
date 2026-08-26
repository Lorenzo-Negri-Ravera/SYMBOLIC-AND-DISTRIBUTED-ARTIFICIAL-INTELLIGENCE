package it.unige.cleaningrobots;

import io.sarl.api.core.AgentTask;
import io.sarl.api.core.DefaultContextInteractions;
import io.sarl.api.core.Destroy;
import io.sarl.api.core.Initialize;
import io.sarl.api.core.Lifecycle;
import io.sarl.api.core.Logging;
import io.sarl.api.core.Schedules;
import io.sarl.lang.core.Address;
import io.sarl.lang.core.Agent;
import io.sarl.lang.core.AtomicSkillReference;
import io.sarl.lang.core.DynamicSkillProvider;
import io.sarl.lang.core.Event;
import io.sarl.lang.core.Scope;
import io.sarl.lang.core.annotation.ImportedCapacityFeature;
import io.sarl.lang.core.annotation.PerceptGuardEvaluator;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import io.sarl.lang.core.util.SerializableProxy;
import jakarta.inject.Inject;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.XbaseGenerated;

@SarlSpecification("0.15")
@SarlElementType(19)
@XbaseGenerated
@SuppressWarnings("all")
public class EnvironmentAgent extends Agent {
  private int seed = 42;

  private Random random = new Random(this.seed);

  private int spawn_rate_dirt = 4000;

  private int count_gardbage = 0;

  private ArrayList<String> gardbageList = new ArrayList<String>();

  private void $behaviorUnit$Initialize$0(final Initialize occurrence) {
    int xIncinerator = (GlobalConfig.GRID_SIZE / 2);
    int yIncinerator = (GlobalConfig.GRID_SIZE / 2);
    Lifecycle _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER();
    _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER.spawn(IncineratorAgent.class, Integer.valueOf(xIncinerator), Integer.valueOf(yIncinerator));
    Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.info(((("Incinerator Agent spawned at position " + Integer.valueOf(xIncinerator)) + ", ") + Integer.valueOf(yIncinerator)));
    IntegerRange _upTo = new IntegerRange(1, GlobalConfig.NUM_AGENTS);
    for (final Integer i : _upTo) {
      {
        int xCleaner = this.random.nextInt(GlobalConfig.GRID_SIZE);
        int yCleaner = this.random.nextInt(GlobalConfig.GRID_SIZE);
        while (((xCleaner == xIncinerator) && (yCleaner == yIncinerator))) {
          {
            xCleaner = this.random.nextInt(GlobalConfig.GRID_SIZE);
            yCleaner = this.random.nextInt(GlobalConfig.GRID_SIZE);
          }
        }
        Lifecycle _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER_1 = this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER();
        _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER_1.spawn(ExplorerAgentt.class, Integer.valueOf(xCleaner), Integer.valueOf(yCleaner));
        Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_1 = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
        _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_1.info(((((("Explorer Agent " + i) + " spawned at position ") + Integer.valueOf(xCleaner)) + ", ") + Integer.valueOf(yCleaner)));
      }
    }
    Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_1 = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_1.info("All agents have been initialized.");
    Schedules _$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER();
    AgentTask spawnTask = _$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER.task("garbage_spawner");
    Schedules _$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER_1 = this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER();
    final Procedure1<Agent> _function = (Agent it) -> {
      if ((this.count_gardbage < GlobalConfig.MAX_DIRT)) {
        int XDirt = this.random.nextInt(GlobalConfig.GRID_SIZE);
        int YDirt = this.random.nextInt(GlobalConfig.GRID_SIZE);
        String _plus = (Integer.valueOf(XDirt) + ",");
        String newDirt = (_plus + Integer.valueOf(YDirt));
        boolean IncineratorPos = ((XDirt == xIncinerator) && (YDirt == yIncinerator));
        if (((!this.gardbageList.contains(newDirt)) && (!IncineratorPos))) {
          this.gardbageList.add(newDirt);
          this.count_gardbage++;
          Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_2 = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
          _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_2.info((((((("SPAWN GARBAGE: new item at " + newDirt) + " (") + Integer.valueOf(this.count_gardbage)) + "/") + Integer.valueOf(GlobalConfig.MAX_DIRT)) + 
            ")"));
        }
      } else {
        Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_3 = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
        _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_3.info((("Max garbage generation reached (" + Integer.valueOf(GlobalConfig.MAX_DIRT)) + "). Stopping spawner."));
        Schedules _$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER_2 = this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER();
        _$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER_2.cancel(spawnTask);
      }
    };
    _$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER_1.every(spawnTask, this.spawn_rate_dirt, _function);
  }

  private void $behaviorUnit$NextCellMovement$1(final NextCellMovement occurrence) {
    String _plus = (Integer.valueOf(occurrence.x) + ",");
    String pos = (_plus + Integer.valueOf(occurrence.y));
    boolean dirtFound = false;
    boolean _contains = this.gardbageList.contains(pos);
    if (_contains) {
      dirtFound = true;
    }
    DefaultContextInteractions _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
    EnvironmentPerception _environmentPerception = new EnvironmentPerception(occurrence.x, occurrence.y, dirtFound);
    class $SerializableClosureProxy implements Scope<Address> {
      
      private final Address $_source;
      
      public $SerializableClosureProxy(final Address $_source) {
        this.$_source = $_source;
      }
      
      @Override
      public boolean matches(final Address it) {
        return Objects.equals(it, $_source);
      }
    }
    final Scope<Address> _function = new Scope<Address>() {
      @Override
      public boolean matches(final Address it) {
        Address _source = occurrence.getSource();
        return Objects.equals(it, _source);
      }
      private Object writeReplace() throws ObjectStreamException {
        return new SerializableProxy($SerializableClosureProxy.class, occurrence.getSource());
      }
    };
    _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_environmentPerception, _function);
  }

  private void $behaviorUnit$PickGarbage$2(final PickGarbage occurrence) {
    String _plus = (Integer.valueOf(occurrence.x) + ",");
    String pos = (_plus + Integer.valueOf(occurrence.y));
    this.gardbageList.remove(pos);
    Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
    int _size = this.gardbageList.size();
    _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.info(((("PICK: Garbage collected at " + pos) + ". Remaining in environment: ") + Integer.valueOf(_size)));
  }

  private void $behaviorUnit$Destroy$3(final Destroy occurrence) {
    Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.info("Environment agent terminated.");
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
  @ImportedCapacityFeature(Lifecycle.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE;

  @SyntheticMember
  @Pure
  private Lifecycle $CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE == null || this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE.get() == null) {
      this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE = $getSkill(Lifecycle.class);
    }
    return $castSkill(Lifecycle.class, this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE);
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Destroy(final Destroy occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Destroy$3(occurrence));
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
  private void $guardEvaluator$NextCellMovement(final NextCellMovement occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$NextCellMovement$1(occurrence));
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$PickGarbage(final PickGarbage occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$PickGarbage$2(occurrence));
  }

  @SyntheticMember
  @Override
  public void $getSupportedEvents(final Set<Class<? extends Event>> toBeFilled) {
    super.$getSupportedEvents(toBeFilled);
    toBeFilled.add(Destroy.class);
    toBeFilled.add(Initialize.class);
    toBeFilled.add(NextCellMovement.class);
    toBeFilled.add(PickGarbage.class);
  }

  @SyntheticMember
  @Override
  public boolean $isSupportedEvent(final Class<? extends Event> event) {
    if (Destroy.class.isAssignableFrom(event)) {
      return true;
    }
    if (Initialize.class.isAssignableFrom(event)) {
      return true;
    }
    if (NextCellMovement.class.isAssignableFrom(event)) {
      return true;
    }
    if (PickGarbage.class.isAssignableFrom(event)) {
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
    if (Destroy.class.equals(eventType)) {
      final var occurrence = (Destroy) event;
      $guardEvaluator$Destroy(occurrence, callbacks);
    }
    if (Initialize.class.equals(eventType)) {
      final var occurrence = (Initialize) event;
      $guardEvaluator$Initialize(occurrence, callbacks);
    }
    if (NextCellMovement.class.equals(eventType)) {
      final var occurrence = (NextCellMovement) event;
      $guardEvaluator$NextCellMovement(occurrence, callbacks);
    }
    if (PickGarbage.class.equals(eventType)) {
      final var occurrence = (PickGarbage) event;
      $guardEvaluator$PickGarbage(occurrence, callbacks);
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
    EnvironmentAgent other = (EnvironmentAgent) obj;
    if (other.seed != this.seed)
      return false;
    if (other.spawn_rate_dirt != this.spawn_rate_dirt)
      return false;
    if (other.count_gardbage != this.count_gardbage)
      return false;
    return super.equals(obj);
  }

  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Integer.hashCode(this.seed);
    result = prime * result + Integer.hashCode(this.spawn_rate_dirt);
    result = prime * result + Integer.hashCode(this.count_gardbage);
    return result;
  }

  @SyntheticMember
  public EnvironmentAgent(final UUID parentID, final UUID agentID) {
    super(parentID, agentID);
  }

  @SyntheticMember
  @Inject
  public EnvironmentAgent(final UUID parentID, final UUID agentID, final DynamicSkillProvider skillProvider) {
    super(parentID, agentID, skillProvider);
  }
}
