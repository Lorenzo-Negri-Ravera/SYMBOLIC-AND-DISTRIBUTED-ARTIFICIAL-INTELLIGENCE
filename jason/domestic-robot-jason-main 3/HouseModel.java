import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;
import java.util.*;

/** class that implements the Model of Domestic Robot application */
public class HouseModel extends GridWorldModel {

    // constants for the grid objects
    public static final int FRIDGE = 16;
    public static final int OWNER  = 32;
    public static final int DOOR  = 64;
    public static final int DIRT = 8;

    // the grid size
    public static final int GSize = 20;

    boolean fridgeOpen   = false; // whether the fridge is open
    boolean carryingBeer = false; // whether the robot is carrying beer
    int sipCount        = 0; // how many sip the owner did
    int availableBeers  = 2; // how many beers are available
    
    // VARIABILI BENCHMARK E SPAZZATURA
    int dirtCount      = 0; 
    int maxDirt = 15; 
    public int cleanedDirtCount = 0;
    public long startTime = 0;
    public boolean benchmarkFinished = false;   
    double dirtSpawnProbability = 0.3; // probability to spawn dirt

    Location lFridge = new Location(0,0);
    Location lOwner  = new Location(GSize-1,GSize-1);
    Location lDoor  = new Location(0,GSize-1);

    // Le 3 basi dei Cleaner
    Location lRobotCleaner1 = new Location(1,0); 
    Location lRobotCleaner2 = new Location(6,0);
    Location lRobotCleaner3 = new Location(0,5);
    Location lRobotConcierge = new Location(GSize/2,0);

    List<Location> dirtLoc = new ArrayList<Location>();
    
    // RISOLTO PROBLEMA 1: Dichiaro solo la variabile, la assegno dopo!
    Location lDirt; 

    public HouseModel() {
        // create a 7x7 grid with 5 mobile agents (Robot, Concierge, 3 Cleaners)
        super(GSize, GSize, 5);

        // RISOLTO PROBLEMA 2: Avvio il timer!
        startTime = System.currentTimeMillis();

        // ag code 0 means the robot (owner's fetcher)
        setAgPos(0, GSize/2, GSize/2);
        
        // ag code 1 means cleaner 1
        setAgPos(1, lRobotCleaner1);
        
        // ag code 2 means the concierge
        setAgPos(2, lRobotConcierge);
        
        // ag code 3 means cleaner 2
        setAgPos(3, lRobotCleaner2);
        
        // ag code 4 means cleaner 3
        setAgPos(4, lRobotCleaner3);

        // initial location of fridge and owner and door
        add(FRIDGE, lFridge);
        add(OWNER, lOwner);
        add(DOOR, lDoor);

        // initial location of dirt randomly generated
        lDirt = super.getFreePos(); // Ora è sicuro chiamarlo!
        add(DIRT, lDirt);
        dirtLoc.add(lDirt);
        dirtCount = 1; // IMPORTANTE: Segnaliamo che ne è già nato uno!
    }

    void spawnDirt() {
        // RISOLTO PROBLEMA 3: Uso il minore stretto (<) per fermarmi esattamente a 15
        if (dirtCount < maxDirt && new Random().nextFloat() < dirtSpawnProbability) {
            
            Location newDirt = super.getFreePos();

            // Evita di spawnare sporco su mobili o agenti
            while(newDirt.equals(lFridge) || newDirt.equals(lOwner) || newDirt.equals(lDoor) || 
                  newDirt.equals(getAgPos(0)) || newDirt.equals(getAgPos(1)) || 
                  newDirt.equals(getAgPos(2)) || newDirt.equals(getAgPos(3)) || 
                  newDirt.equals(getAgPos(4))) {
                
                newDirt = super.getFreePos();
            }
            
            add(DIRT, newDirt);
            dirtLoc.add(newDirt);
            dirtCount++;
            System.out.println("SPAWN GARBAGE: new item at " + newDirt.x + "," + newDirt.y + " (" + dirtCount + "/" + maxDirt + ")");
            
        } else if (dirtCount == maxDirt) {
            System.out.println("Max garbage generation reached (" + maxDirt + "). Stopping spawner.");
            dirtCount++; // Lo incremento a 16 così questa scritta non si ripete all'infinito
        }
    }

    boolean openFridge() {
        spawnDirt();
        if (!fridgeOpen) {
            fridgeOpen = true;
            return true;
        } else {
            return false;
        }
    }

    boolean closeFridge() {
        spawnDirt();
        if (fridgeOpen) {
            fridgeOpen = false;
            return true;
        } else {
            return false;
        }
    }

    boolean moveRobot(Location dest) {
        Location r1 = getAgPos(0);
        if (r1.x < dest.x)        r1.x++;
        else if (r1.x > dest.x)   r1.x--;
        if (r1.y < dest.y)        r1.y++;
        else if (r1.y > dest.y)   r1.y--;
        setAgPos(0, r1); // move the robot in the grid

        if (view != null) {
            view.update(lFridge.x,lFridge.y);
            view.update(lOwner.x,lOwner.y);
        }
        return true;
    }

    // Metodo aggiornato con Timer Benchmark
    boolean moveRobotCleaner(Location dest, int agId) {
        Location r1 = getAgPos(agId);
        
        // Movimento del robot
        if (r1.x < dest.x)        r1.x++;
        else if (r1.x > dest.x)   r1.x--;
        if (r1.y < dest.y)        r1.y++;
        else if (r1.y > dest.y)   r1.y--;
        
        setAgPos(agId, r1); // muove lo specifico robot cleaner

        // LOGICA DI PULIZIA E BENCHMARK (TIMER)
        if (hasObject(DIRT, r1)) {
            remove(DIRT, r1); // Rimuove dal modello visivo
            dirtLoc.remove(r1); // Rimuove dalla lista logica
            
            // Incrementa il contatore di spazzatura PULITA
            cleanedDirtCount++;
            System.out.println("[CLEANER " + agId + "] Dirt removed at " + r1.x + "," + r1.y + "! Total cleaned: " + cleanedDirtCount + "/" + maxDirt);

            // CONTROLLO DEL TIMER: Se ha raggiunto 15 e non aveva ancora finito
            if (cleanedDirtCount >= maxDirt && !benchmarkFinished) {
                benchmarkFinished = true;
                
                // Calcolo del tempo esatto
                long totalTimeMillis = System.currentTimeMillis() - startTime;
                long totalSeconds = totalTimeMillis / 1000;
                long minutes = totalSeconds / 60;
                long seconds = totalSeconds % 60;

                System.out.println("=================================================");
                System.out.println(">>> BENCHMARK JASON completed! <<<");
                System.out.println("All  " + maxDirt + " garbage has been cleaned");
                System.out.println("Total time: " + minutes + " minutes and " + seconds + " seconds (" + totalSeconds + "s)");
                System.out.println("=================================================");
            }
        }

        if (view != null) {
            view.update();
        }
        
        return true;
    }

    boolean moveRobotConcierge(Location dest) {
        Location r1 = getAgPos(2);
        if (r1.x < dest.x)        r1.x++;
        else if (r1.x > dest.x)   r1.x--;
        if (r1.y < dest.y)        r1.y++;
        else if (r1.y > dest.y)   r1.y--;
        setAgPos(2, r1); 

        if (view != null) {
            view.update();
        }
        return true;
    }

    boolean getBeer() {
        spawnDirt();
        Random rn = new Random();
        if (fridgeOpen && availableBeers > 0 && !carryingBeer) {
            availableBeers--;
            carryingBeer = true;
            if (view != null)
                view.update(lFridge.x,lFridge.y);
            return true;
        } else {
            return false;
        }
    }

    boolean addBeer(int n) {
        spawnDirt();
        availableBeers += n;
        if (view != null)
            view.update(lFridge.x,lFridge.y);
        return true;
    }

    boolean handInBeer() {
        spawnDirt();
        if (carryingBeer) {
            sipCount = 15; // Cambiato a 15 per allinearlo al resto se necessario
            carryingBeer = false;
            if (view != null)
                view.update(lOwner.x,lOwner.y);
            return true;
        } else {
            return false;
        }
    }

    boolean sipBeer() {
        if (sipCount > 0) {
            sipCount--;
            if (view != null)
                view.update(lOwner.x,lOwner.y);
            return true;
        } else {
            return false;
        }
    }
}