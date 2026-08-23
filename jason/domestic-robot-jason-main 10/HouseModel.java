import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;
import java.util.*;

/** class that implements the Model of Domestic Robot application */
public class HouseModel extends GridWorldModel {

    // constants for the grid objects
    public static final int FRIDGE = 16;
    public static final int OWNER  = 32;
    public static final int DOOR   = 64;
    public static final int DIRT   = 8;

    // the grid size
    public static final int GSize = 20;

    boolean fridgeOpen   = false;
    boolean carryingBeer = false;
    int sipCount         = 0;
    int availableBeers   = 2;
    
    // VARIABILI BENCHMARK E SPAZZATURA
    int dirtCount        = 0; 
    int maxDirt          = 15; 
    public int cleanedDirtCount = 0;
    public long startTime = 0;
    public boolean benchmarkFinished = false;   
    double dirtSpawnProbability = 0.3;

    Location lFridge = new Location(0,0);
    Location lOwner  = new Location(GSize-1, GSize-1);
    Location lDoor   = new Location(0, GSize-1);
    Location lRobotConcierge = new Location(GSize/2, 0);

    List<Location> dirtLoc = new ArrayList<Location>();
    Location lDirt; 

    public HouseModel() {
        super(GSize, GSize, 12);

        startTime = System.currentTimeMillis();

        // ag code 0: robot fetcher
        setAgPos(0, GSize/2, GSize/2);
        
        // ag code 2: concierge
        setAgPos(2, lRobotConcierge);
        
        // Posizionamento dei 10 Cleaner in celle libere
        int[] cleanerIds = {1, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        for (int id : cleanerIds) {
            setAgPos(id, super.getFreePos());
        }

        add(FRIDGE, lFridge);
        add(OWNER, lOwner);
        add(DOOR, lDoor);

        lDirt = super.getFreePos();
        add(DIRT, lDirt);
        dirtLoc.add(lDirt);
        dirtCount = 1;
    }

    void spawnDirt() {
        if (dirtCount < maxDirt && new Random().nextFloat() < dirtSpawnProbability) {
            
            Location newDirt = super.getFreePos();
            boolean validSpawn = false;

            while (!validSpawn) {
                validSpawn = true;
                if (newDirt.equals(lFridge) || newDirt.equals(lOwner) || newDirt.equals(lDoor) || 
                    newDirt.equals(getAgPos(0)) || newDirt.equals(getAgPos(2))) {
                    validSpawn = false;
                }
                
                int[] cleanerIds = {1, 3, 4, 5, 6, 7, 8, 9, 10, 11};
                for (int id : cleanerIds) {
                    if (getAgPos(id) != null && newDirt.equals(getAgPos(id))) {
                        validSpawn = false;
                    }
                }
                
                if (!validSpawn) {
                    newDirt = super.getFreePos();
                }
            }
            
            add(DIRT, newDirt);
            dirtLoc.add(newDirt);
            dirtCount++;
            System.out.println("SPAWN GARBAGE: new item at " + newDirt.x + "," + newDirt.y + " (" + dirtCount + "/" + maxDirt + ")");
            
        } else if (dirtCount == maxDirt) {
            System.out.println("Max garbage generation reached (" + maxDirt + "). Stopping spawner.");
            dirtCount++; 
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
        setAgPos(0, r1);

        if (view != null) {
            view.update(lFridge.x, lFridge.y);
            view.update(lOwner.x, lOwner.y);
        }
        return true;
    }

    boolean moveRobotCleaner(Location dest, int agId) {
        Location r1 = getAgPos(agId);
        
        if (r1.x < dest.x)        r1.x++;
        else if (r1.x > dest.x)   r1.x--;
        if (r1.y < dest.y)        r1.y++;
        else if (r1.y > dest.y)   r1.y--;
        
        setAgPos(agId, r1);

        if (hasObject(DIRT, r1)) {
            remove(DIRT, r1);
            dirtLoc.remove(r1);
            
            cleanedDirtCount++;
            System.out.println("[CLEANER " + agId + "] Dirt removed at " + r1.x + "," + r1.y + "! Total cleaned: " + cleanedDirtCount + "/" + maxDirt);

            if (cleanedDirtCount >= maxDirt && !benchmarkFinished) {
                benchmarkFinished = true;
                
                long totalTimeMillis = System.currentTimeMillis() - startTime;
                long totalSeconds = totalTimeMillis / 1000;
                long minutes = totalSeconds / 60;
                long seconds = totalSeconds % 60;

                System.out.println("=================================================");
                System.out.println(">>> BENCHMARK JASON COMPLETATO! <<<");
                System.out.println("All " + maxDirt + " garbages have been cleaned.");
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
                view.update(lFridge.x, lFridge.y);
            return true;
        } else {
            return false;
        }
    }

    boolean addBeer(int n) {
        spawnDirt();
        availableBeers += n;
        if (view != null)
            view.update(lFridge.x, lFridge.y);
        return true;
    }

    boolean handInBeer() {
        spawnDirt();
        if (carryingBeer) {
            sipCount = 10;
            carryingBeer = false;
            if (view != null)
                view.update(lOwner.x, lOwner.y);
            return true;
        } else {
            return false;
        }
    }

    boolean sipBeer() {
        if (sipCount > 0) {
            sipCount--;
            if (view != null)
                view.update(lOwner.x, lOwner.y);
            return true;
        } else {
            return false;
        }
    }
}