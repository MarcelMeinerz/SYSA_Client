package zickzack;

import multiagent.remote.IStrategy;
import gameclient.AgentUtils;

import java.util.ArrayList;
import java.awt.List;
import java.awt.Point;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import multiagent.remote.IAgent;

public class Strategy extends UnicastRemoteObject implements IStrategy, Serializable {
	
	boolean presented;
	boolean isReturning;
	int distance;
	int distanceStartValue;
	int directionValue; //1 == nach rechts, -1 == nach links
	boolean goingUp;
	boolean goingDown;
	boolean goToNext;
    
    /**
     * Idee dieser Strategie ist, dass sich ein Roboter in einer Zick-Zack-Bewegung von oben nach unten und von unten nach oben über das Spielfeld bewegt, um möglichst alle Felder nur einmal zu besuchen. 
     * Findet er unterwegs Rohstoffe, nimmt er diese auf. Ist er voll beladen, begibt er sich auf direktem Weg zur Abladestelle, lädt alles ab und macht sich auf den Weg zurück zum zuletzt besuchten Feld. 
     * Auf diesem angekommen, setzt er seine Zick-Zick-Bewegung fort. Findet er auf seinem Rückweg Ressourcen, sammelt er auch diese ein. 
     * Ist der Roboter am einen Ende des Spielfeldes angekommen, steuert er ein definiertes Feld in der Mitte des Spielfeldes an, um von dort seine Zick-Zack-Bewegung in die andere Richtung fortzusetzen.
     * Wenn genügend Ressourcen vorhanden sind, um einen neuen Roboter zu erwerben, noch über die Hälfte der zum Sieg benötigten Punkte fehlt, und erst ein Roboter vorhanden, wird ein Kauf getätigt. 
     * Der neue Roboter startet die Zick-Zack-Bewegung in derselben Art und Weise wie der erste Roboter und sammelt so Ressourcen ein, die durch blockierte Felder nicht gesammelt werden konnten, oder die von anderen Spielern abgeladen wurden.
     * Ein Vorteil dieser Strategie ist, dass fast alle Felder des Spielfeldes einmal besucht werden.
     * Nachteil dieser Strategie im Vergleich zur Schnecken-Strategie ist, dass aufgrund der Zick-Zack-Bewegung über das Spielfeld bereits zu Beginn größere Anfahrtswege zum Abladen der eingesammelten Ressourcen zurückzulegen sind.
     */
    private static final long serialVersionUID = 1L;
    
    
    public Strategy() throws RemoteException {
        super();
        System.out.println("Strategie Schnecke gestartet");
        presented = false;
        distanceStartValue = 1;
        distance = distanceStartValue;
        directionValue = 1;
        goingDown = true;
        goingUp = false;
        isReturning = true;
        goToNext = false;
    }

    @Override
    public void nextAction(IAgent agent) throws RemoteException {
    	if (!presented) {
            System.out.println("Player: " + agent.getName());
            System.out.println("Size: " + agent.getRememberFieldSize());
            System.out.println("Home: " + agent.getHomeXY() + " Home/2: " + agent.getHomeXY()/2);
            presented = true;
        	int home = agent.getHomeXY();
            setReturnToPoint(agent, home + distance, 0);
            /*IAgent[] agentArray = agent.getAgentArray();
            for (int a=0; a<agentArray.length; a++) {
    			if (agentArray[a] == agent)
    				System.out.println("Nr.: " + a);
    			else
    				System.out.println("Nr. not " + a);
    		}*/
    	}
    	
        agent.take(); //Default Aktion
        
        if (agent.getLoad() >= agent.getCapacity()){
            //Agent voll beladen
        	//System.out.println(agent.getName() + " geht nach Hause.");
        	int home = agent.getHomeXY();
        	String direction = "";
        	
        	if ((agent.getPosx() > home+1) && (agent.requestField(AgentUtils.LEFT))){
        		direction = AgentUtils.LEFT;
        	} else if ((agent.getPosx() < home-1) && (agent.requestField(AgentUtils.RIGHT))){
        		direction = AgentUtils.RIGHT;
        	}
        	
        	if ((agent.getPosy() > home+1) && (agent.requestField(AgentUtils.TOP))){
        		direction = AgentUtils.TOP;
        	} else if ((agent.getPosy() < home-1) && (agent.requestField(AgentUtils.BOTTOM))){
        		direction = AgentUtils.BOTTOM;
        	} 
        	
        	if (agent.checkIfOnSpawn()){
        		agent.put();
        		isReturning = true;
        	} else {
        		if (direction == "") {
		    		//System.out.println(agent.getName() + " will nach Hause, kommt nicht durch und l�uft deshalb bl�d herum.");
		        	//zuf�llige Richtung festlegen
		        	direction = getRandomDirection(agent);
        		}
            	agent.go(direction);
        		setUndoPreviousDirection(agent, direction);
        	}
        } else if (agent.check() > 0) {
        	//einsammeln
            agent.take();
            
            if (!isReturning && agent.getLoad() + 1 == agent.getCapacity()) {
	            //nach diesem Zug ist Agent voll beladen
	            //letzte Position in customData merken, um sp�ter dort weiter machen zu k�nnen            
	            setReturnToPoint(agent, agent.getPosx(), agent.getPosy());
	            
	            if (agent.check() == 1) {
	            	//Feld nach Aufnehmen leer --> mit n�chstem Feld weitermachen/zu n�chstem Feld zur�ckkehren
	            	//goToNext = true;
	            	resetReturnToPoint(agent);
	            }
            }
        } else if (agent.getLoad() > 0 && agent.checkIfOnSpawn()) {
        	//Agent hat Ressourcen geladen (Kapazit�tsgrenze aber noch nicht erreicht)
        	//und befindet sich auf Spawn-Feld --> abladen
        	agent.put();
        } else if(agent.buyPossible() && agent.getAgentArray().length == 1 && (double)(agent.getPoints() + agent.getLoad())/agent.getTargetAmount() < 0.5){
        	//Kauf eines neuen Agenten m�glich
        	agent.buy();
        	return;
        } else {
        	//(hier immer: aktuelle Position = 0 Ressourcen)
        	int home = agent.getHomeXY();

        	//Punkt zum Zur�ckkehren nach Abladen bestimmen
            Point returnTo = getReturnToPoint(agent);
            //Ziel-Punkt
            Point goal;
            
            //�u�eres Ende der ZickZack-Bewegung erreicht --> nach innen zur�ckkehren und ZickZack in die andere Richtung starten
            if (distance == (home +  1) * directionValue) {
            	System.out.println("Richtung �ndern. distance = " + distance);
            	distance = distanceStartValue * (-1);
            	distanceStartValue = distance;
            	directionValue = directionValue * (-1);
                setReturnToPoint(agent, home + distance, 0);
            	isReturning = true;
            } 
            
            if (isReturning && (agent.getPosx() != returnTo.x || agent.getPosy() != returnTo.y)) {
            	//ist am Zur�ckkehren und hat Punkt noch nicht erreicht
            	goal = returnTo;
            	//System.out.println("Return from (" + agent.getPosx() + "|" + agent.getPosy() + ") to " + returnTo);
            } else {
            	//Punkt zum Fortsetzen der Bewegung bestimmen
            	setContinuePoint(agent);
            	goal = getContinuePoint(agent);
            	isReturning = false;
            	//System.out.println("Goal: from (" + agent.getPosx() + "|" + agent.getPosy() + ") to " + goal);
            }
            
            String direction = "";
            
            if (goal.x != -1 && goal.y != -1) {
            	
            	//Richtung anhand des Ziels festlegen
            	if (goal.x < agent.getPosx()) {
            		if (agent.requestField(AgentUtils.LEFT))
            			direction = AgentUtils.LEFT;
            	} else if (goal.x > agent.getPosx()) {
            		if (agent.requestField(AgentUtils.RIGHT))
            			direction = AgentUtils.RIGHT;
            	}
            	
            	if (goal.y < agent.getPosy()) {
            		if (agent.requestField(AgentUtils.TOP))
            			direction = AgentUtils.TOP;
            	} else if (goal.y > agent.getPosy()) {
            		if (agent.requestField(AgentUtils.BOTTOM))
            			direction = AgentUtils.BOTTOM;
            	} 
            	
            	if (direction == "" || !agent.requestField(direction)) {
                	//System.out.println(agent.getName() + " kommt nicht durch und l�uft deshalb bl�d herum.");
                	//zuf�llige Richtung festlegen
                	direction = getRandomDirection(agent);
                	if (!isReturning) {
                		isReturning = true;
                		setReturnToPoint(agent, getContinuePoint(agent));
                		//zu n�chstem Feld zur�ckkehren
                		resetReturnToPoint(agent);
            		}
            	} else {
            		//System.out.println(agent.getName() + " geht zu: (" + goal.x + "|" + goal.y + ") �ber " + direction + ".");                	
            	}
            	
            } else {
            	//System.out.println(agent.getName() + " l�uft bl�d herum.");
            	//zuf�llige Richtung festlegen
            	direction = getRandomDirection(agent);
            }
            agent.go(direction);
    		setUndoPreviousDirection(agent, direction);
        }
    }

    public void setReturnToPoint(IAgent agent, int x, int y) {   	
        agent.setCustomData(2, 0, x);
        agent.setCustomData(2, 1, y);

    	//System.out.println("Return Point: (" + x + "|" + y + ")");	
    }
    
    public void resetReturnToPoint(IAgent agent) {  
    	int home = agent.getHomeXY();
    	int size = agent.getRememberFieldSize();
    	String undo = getUndoPreviousDirection(agent);
    	Point p = getReturnToPoint(agent);
    	int x = p.x;
    	int y = p.y;
		
    	if ((x == 0 || x == size-1) && (y == 0 || y == size-1) && (undo == AgentUtils.BOTTOM || undo == AgentUtils.TOP)) {
			//ZickZack beendet
			//Distanz erh�hen
			distance += 1;
			x -= 1 * directionValue;
		} else if (y == 0 && undo == AgentUtils.BOTTOM) {
			//oben, kam von unten
			x += 1 * directionValue;
		} else if (y == size - 1 && undo == AgentUtils.TOP) {
			//unten, kam von oben
			x += 1 * directionValue;
		} else if (y == 0 && undo != AgentUtils.BOTTOM) {
			//oben, kam von seitlich
			y += 1;
			//Distanz erh�hen
			distance = x - home;
	        goingDown = true;
	        goingUp = false;
		} else if (y == size - 1 && undo != AgentUtils.TOP) {
			//unten, kam von seitlich
			y -= 1;
			//Distanz erh�hen
			distance = x - home;
	        goingDown = false;
	        goingUp = true;
		} else if (undo == AgentUtils.BOTTOM) {
			//kam von unten --> weiter nach oben gehen
			y -= 1;
		} else if (undo == AgentUtils.TOP) {
			//kam von oben --> weiter nach unten gehen
			y += 1;
		} else if (goingUp) {
			y -= 1;
		} else if (goingDown) {
			y += 1;
		} else {
			System.out.println("No case fitted: x=" + x + ", y=" + y + ", home=" + home + ", distance=" + distance );
		}

		setReturnToPoint(agent, x, y);
    }
    
    public void setReturnToPoint(IAgent agent, Point p) {   	
        agent.setCustomData(2, 0, p.x);
        agent.setCustomData(2, 1, p.y);

    	//System.out.println("Return Point: (" + p.x + "|" + p.y + ")");	
    }
    
    public Point getReturnToPoint(IAgent agent) {   	
    	int xPos = agent.getCustomData(2, 0);
    	int yPos = agent.getCustomData(2, 1);
    	
    	return new Point(xPos, yPos);    	
    }
    
    public void setContinuePoint(IAgent agent) {  
    	int home = agent.getHomeXY();
    	int size = agent.getRememberFieldSize();
    	String undo = getUndoPreviousDirection(agent);
		int x = agent.getPosx();
		int y = agent.getPosy();
		
		if ((x == 0 || x == size-1) && (y == 0 || y == size-1) && (undo == AgentUtils.BOTTOM || undo == AgentUtils.TOP)) {
			//ZickZack beendet
			//Distanz erh�hen
			distance += 1 * directionValue;
			x -= 1 * directionValue;
		} else if (y == 0 && undo == AgentUtils.BOTTOM) {
			//oben, kam von unten
			x += 1 * directionValue;
		} else if (y == size - 1 && undo == AgentUtils.TOP) {
			//unten, kam von oben
			x += 1 * directionValue;
		} else if (y == 0 && undo != AgentUtils.BOTTOM) {
			//oben, kam von seitlich
			y += 1;
			//Distanz erh�hen
			distance = x - home;
	        goingDown = true;
	        goingUp = false;
		} else if (y == size - 1 && undo != AgentUtils.TOP) {
			//unten, kam von seitlich
			y -= 1;
			//Distanz erh�hen
			distance = x - home;
	        goingDown = false;
	        goingUp = true;
		} else if (undo == AgentUtils.BOTTOM) {
			//kam von unten --> weiter nach oben gehen
			y -= 1;
		} else if (undo == AgentUtils.TOP) {
			//kam von oben --> weiter nach unten gehen
			y += 1;
		} else if (goingUp) {
			y -= 1;
		} else if (goingDown) {
			y += 1;
		} else {
			System.out.println("No case fitted: x=" + x + ", y=" + y + ", home=" + home + ", distance=" + distance );
		}
    	
        agent.setCustomData(1, 0, x);
        agent.setCustomData(1, 1, y);	

    	//System.out.println("Continue Point: (" + x + "|" + y + ")");
    }
   
    public Point getContinuePoint(IAgent agent) {   	
    	int xPos = agent.getCustomData(1, 0);
    	int yPos = agent.getCustomData(1, 1);
    	
    	return new Point(xPos, yPos);    	
    }
    
    public String getRandomDirection(IAgent agent) {
    	int direction = (int)(Math.random() * 4);
    	String undoDirection = getUndoPreviousDirection(agent);
    	
    	String goTo = "";
    	int counter = 0;
    	    	
    	while (counter < 4) {
    		switch (direction){
        	case 0:
        		if (agent.requestField(AgentUtils.LEFT) && !fieldIsSpawn(agent, AgentUtils.LEFT) && AgentUtils.LEFT != undoDirection) {
                    goTo = AgentUtils.LEFT;
                    return goTo;
                }
        		break;
        	case 1:
        		if (agent.requestField(AgentUtils.TOP) && !fieldIsSpawn(agent, AgentUtils.TOP) && AgentUtils.TOP != undoDirection) {
        			goTo = AgentUtils.TOP;
                    return goTo;
                }
        		break;
        	case 2:
        		if (agent.requestField(AgentUtils.RIGHT) && !fieldIsSpawn(agent, AgentUtils.RIGHT) && AgentUtils.RIGHT != undoDirection) {
        			goTo = AgentUtils.RIGHT;
                    return goTo;
                }
        		break;
        	case 3:
        		if (agent.requestField(AgentUtils.BOTTOM) && !fieldIsSpawn(agent, AgentUtils.BOTTOM) && AgentUtils.BOTTOM != undoDirection) {
        			goTo = AgentUtils.BOTTOM;
                    return goTo;
        		}
        		break;
        	}
    		direction = (direction + 1) % 4;
    		counter += 1;
    	}
    	
    	if (goTo == "") {
    		//Undo-Direction versuchen
    		//if (agent.requestField(undoDirection)) {
                goTo = undoDirection;
                return goTo;
            //}
    		//ansonsten ist Agent eingekesselt --> Richtung egal, Agent kann nichts tun
    	}
    	    	
    	return goTo;
    }
    
    public boolean fieldIsSpawn (IAgent agent, String direction) {
    	int x = agent.getPosx();
    	int y = agent.getPosy();
    	
    	switch (direction){
    	case AgentUtils.LEFT:
            x -= 1;
    		break;
    	case AgentUtils.TOP:
    		y -= 1;
    		break;
    	case AgentUtils.RIGHT:
    		x += 1;
    		break;
    	case AgentUtils.BOTTOM:
    		y += 1;
    		break;
    	}
    	
    	int home = agent.getRememberFieldSize() / 2;
    	
    	if ((x >= home-1 && x <= home+1) && (y >= home-1 && y <= home+1)) {
    		return true;
    	} else {
    		return false;
    	}
    }

    public void setUndoPreviousDirection(IAgent agent, String direction) {
    	int undoDirection = 0;
    	
    	switch (direction){
    	case AgentUtils.LEFT:
    		undoDirection = 3; //AgentUtils.RIGHT;
    		break;
    	case AgentUtils.TOP:
    		undoDirection = 4; //AgentUtils.BOTTOM;
    		break;
    	case AgentUtils.RIGHT:
    		undoDirection = 1; //AgentUtils.LEFT;
    		break;
    	case AgentUtils.BOTTOM:
    		undoDirection = 2; //AgentUtils.TOP;
    		break;
    	}
    	
    	agent.setCustomData(0, 0, undoDirection);
    }

    public String getUndoPreviousDirection(IAgent agent) {
    	int undo = agent.getCustomData(0, 0);
    	
    	String undoDirection = "";
    	
    	switch (undo){
    	case 1:
    		undoDirection = AgentUtils.LEFT;
    		break;
    	case 2:
    		undoDirection = AgentUtils.TOP;
    		break;
    	case 3:
    		undoDirection = AgentUtils.RIGHT;
    		break;
    	case 4:
    		undoDirection = AgentUtils.BOTTOM;
    		break;
    	}
    	
    	return undoDirection;
    }
    
}
