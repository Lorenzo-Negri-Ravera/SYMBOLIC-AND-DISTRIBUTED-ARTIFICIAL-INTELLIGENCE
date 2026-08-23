import jason.asSyntax.*;
import jason.environment.Environment;
import jason.environment.grid.Location;
import java.util.logging.Logger;

public class HouseEnv extends Environment {

    // common literals
    public static final Literal of  = Literal.parseLiteral("open(fridge)");
    public static final Literal clf = Literal.parseLiteral("close(fridge)");
    public static final Literal gb  = Literal.parseLiteral("get(beer)");
    public static final Literal hb  = Literal.parseLiteral("hand_in(beer)");
    public static final Literal sb  = Literal.parseLiteral("sip(beer)");
    public static final Literal hob = Literal.parseLiteral("has(owner,beer)");

    public static final Literal af = Literal.parseLiteral("at(robot,fridge)");
    public static final Literal ao = Literal.parseLiteral("at(robot,owner)");

    public static final Literal hd = Literal.parseLiteral("has(dirt)");
    public static final Literal arcli = Literal.parseLiteral("at(robotCleaner,init)");

    public static final Literal arci = Literal.parseLiteral("at(robotConcierge,init)");
    public static final Literal ado = Literal.parseLiteral("at(robotConcierge,door)");
    public static final Literal afr = Literal.parseLiteral("at(robotConcierge,fridge)");

    static Logger logger = Logger.getLogger(HouseEnv.class.getName());

    HouseModel model; // the model of the grid

    // Array contenente i nomi di tutti e 10 i pulitori
    private final String[] cleanerNames = {
        "robotCleaner", "robotCleaner2", "robotCleaner3", "robotCleaner4", 
        "robotCleaner5", "robotCleaner6", "robotCleaner7", "robotCleaner8", 
        "robotCleaner9", "robotCleaner10"
    };

    @Override
    public void init(String[] args) {
        model = new HouseModel();

        if (args.length == 1 && args[0].equals("gui")) {
            HouseView view  = new HouseView(model);
            model.setView(view);
        }

        updatePercepts();
    }

    /** creates the agents percepts based on the HouseModel */
    void updatePercepts() {
        // clear the percepts of the agents
        clearPercepts("robot");
        clearPercepts("robotConcierge");
        clearPercepts("owner");
        
        // Pulisce le percezioni di tutti e 10 i pulitori tramite ciclo
        for (String c : cleanerNames) {
            clearPercepts(c);
        }

        // get the locations
        Location lRobot = model.getAgPos(0);
        Location lRobotConcierge = model.getAgPos(2);
        
        // add agent location to its percepts
        if (lRobot.equals(model.lFridge)) addPercept("robot", af);
        if (lRobot.equals(model.lOwner)) addPercept("robot", ao);

        if (lRobotConcierge.equals(model.lDoor)) addPercept("robotConcierge", ado);
        if (lRobotConcierge.equals(model.lFridge)) addPercept("robotConcierge", afr);
        if (lRobotConcierge.equals(model.lRobotConcierge)) addPercept("robotConcierge", arci);

        // Se c'è spazzatura, TUTTI e 10 i robot la percepiscono
        if (model.dirtLoc.size() > 0){
            for (String c : cleanerNames) {
                addPercept(c, hd);
            }
        }

        // add beer "status" the percepts
        if (model.fridgeOpen) {
            addPercept("robot", Literal.parseLiteral("stock(beer,"+model.availableBeers+")"));
        }
        if (model.sipCount > 0) {
            addPercept("robot", hob);
            addPercept("owner", hob);
        }
    }


    @Override
    public boolean executeAction(String ag, Structure action) {
        System.out.println("["+ag+"] doing: "+action);
        boolean result = false;
        
        if (action.equals(of)) { 
            result = model.openFridge();
        } else if (action.equals(clf)) { 
            result = model.closeFridge();
        } else if (action.getFunctor().equals("move_towards")) {
            String l = action.getTerm(0).toString();
            Location dest = null;
            if (l.equals("fridge")) {
                dest = model.lFridge;
            } else if (l.equals("owner")) {
                dest = model.lOwner;
            }
            try {
                result = model.moveRobot(dest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (action.getFunctor().equals("move")) {
            String object = action.getTerm(0).toString();
            String l = action.getTerm(1).toString();
            Location dest = null;

            switch(object) {
                case "robotConcierge":
                    if (l.equals("fridge")) dest = model.lFridge;
                    else if (l.equals("door")) dest = model.lDoor;
                    else if (l.equals("init")) dest = model.lRobotConcierge;

                    try {
                        result = model.moveRobotConcierge(dest);
                    } catch (Exception e) { e.printStackTrace(); }
                break;

                case "robotCleaner":
                    // Calcolo della destinazione
                    if (l.equals("dirt") && model.dirtLoc.size() > 0) {
                        dest = model.dirtLoc.get(0);
                    }

                    try {
                        if (model.dirtLoc.size() > 0 || dest != null) {
                            
                            // Mappatura dinamica ed elegante dell'ID per i 10 robot
                            int idToMove = 1; // Default per il primo "robotCleaner" (ID 1)
                            
                            if (ag.startsWith("robotCleaner") && ag.length() > 12) {
                                // Estrae il numero dalla stringa (es. da "robotCleaner10" estrae "10")
                                int num = Integer.parseInt(ag.substring(12));
                                // Il robot 2 ha ID 3, il robot 10 ha ID 11. Quindi si somma 1 al numero estratto.
                                idToMove = num + 1; 
                            }
                            
                            // Passiamo l'ID corretto al modello
                            result = model.moveRobotCleaner(dest, idToMove);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                break;
            }
        } else if (action.equals(gb)) {
            result = model.getBeer();
        } else if (action.equals(hb)) {
            result = model.handInBeer();
        } else if (action.equals(hd)) {
            result = model.dirtLoc.size() > 0;
        } else if (action.equals(sb)) {
            result = model.sipBeer();
        } else if (action.getFunctor().equals("deliver")) {
            try {
                Thread.sleep(4000);
                result = model.addBeer( (int)((NumberTerm)action.getTerm(1)).solve());
            } catch (Exception e) {
                logger.info("Failed to execute action deliver!"+e);
            }
        } else {
            logger.info("Failed to execute action "+action);
        }

        if (result) {
            updatePercepts();
            try { Thread.sleep(100); } catch (Exception e) {}
        }
        return result;
    }
}