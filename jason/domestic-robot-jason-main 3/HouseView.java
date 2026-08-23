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

        // Disegna i 3 CLEANER (ID 1, 3 e 4)
        else if (id == 1 || id == 3 || id == 4) { 
            c = Color.orange;
            super.drawAgent(g, x, y, c, -1);
            g.setColor(Color.black);
            // Assegna il numero corretto: 1, 2 o 3
            String cleanerName = "Cleaner " + (id == 1 ? "1" : (id == 3 ? "2" : "3"));
            drawString(g, x, y, defaultFont, cleanerName);
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