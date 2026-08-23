import jason.environment.grid.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/** class that implements the View of Domestic Robot application */
public class HouseView extends GridWorldView {

    HouseModel hmodel;

    public HouseView(HouseModel model) {
        super(model, "Domestic Robot", 700);
        hmodel = model;
        defaultFont = new Font("Arial", Font.BOLD, 16); // change default font
        setVisible(true);
        repaint();
    }

    /** draw application objects */
    @Override
    public void draw(Graphics g, int x, int y, int object) {
        Location lRobot = hmodel.getAgPos(0);

        super.drawAgent(g, x, y, Color.lightGray, -1);
        switch (object) {
        case HouseModel.FRIDGE:
            if (lRobot.equals(hmodel.lFridge)) {
                super.drawAgent(g, x, y, Color.green, -1);
            }
            g.setColor(Color.black);
            drawString(g, x, y, defaultFont, "Fridge ("+hmodel.availableBeers+")");
            break;
        case HouseModel.DIRT:
            super.drawObstacle(g, x, y);
            g.setColor(Color.CYAN);
            drawString(g, x, y, new Font("Arial", Font.BOLD, 11), "Dirt");
            break;
        case HouseModel.DOOR:
            super.drawAgent(g, x, y, Color.lightGray, -1);
            g.setColor(Color.BLUE);
            drawString(g, x, y, new Font("Arial", Font.BOLD, 16), "DOOR");
            break;
        case HouseModel.OWNER:
            if (lRobot.equals(hmodel.lOwner)) {
                super.drawAgent(g, x, y, Color.yellow, -1);
            }
            String o = "Owner";
            if (hmodel.sipCount > 0) {
                o +=  " ("+hmodel.sipCount+")";
            }
            g.setColor(Color.black);
            drawString(g, x, y, defaultFont, o);
            break;
        }
        repaint();
    }

    @Override
    public void drawAgent(Graphics g, int x, int y, Color c, int id) {
        
        // Disegna il robot principale (proprietario/cameriere) - ID 0
        if (id == 0) {
            c = Color.yellow;
            if (hmodel.carryingBeer) c = Color.orange;
            super.drawAgent(g, x, y, c, -1);
            g.setColor(Color.black);
            drawString(g, x, y, defaultFont, "Robot");
        }
        // Disegna i 10 CLEANER (ID 1 e ID da 3 a 11)
        else if (id == 1 || (id >= 3 && id <= 11)) { 
            c = Color.orange;
            super.drawAgent(g, x, y, c, -1);
            g.setColor(Color.black);
            
            // Calcola il numero da stampare a schermo
            int cleanerNum = (id == 1) ? 1 : (id - 1);
            drawString(g, x, y, defaultFont, "C" + cleanerNum); // "C1", "C2"... per non occupare troppo spazio
        }

        // Disegna il CONCIERGE (ID 2)
        else if (id == 2) { 
            c = Color.magenta; 
            super.drawAgent(g, x, y, c, -1);
            g.setColor(Color.white);
            drawString(g, x, y, defaultFont, "Concierge");
        }
    }
}