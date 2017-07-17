package eimerkette;


import gameclient.AgentUtils;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import multiagent.remote.IAgent;
import multiagent.remote.IStrategy;
/**
 *
 * @author Marcel_Meinerz (marcel.meinerz@th-bingen.de)
 * @author Steffen_Hollenbach
 * @author Jasmin_Welschbillig
 *
 * @version 1.0
 *
 *
 */
public class Strategy extends UnicastRemoteObject implements IStrategy, Serializable {

    /**
     * Bei dieser Strategie sammelt der Start-Roboter zunächst nach dem Grundschema so viel Rohstoffe, dass ein zweiter Roboter gekauft werden kann. Dabei bewegt er sich zufällig fort.
     * Jeder Roboter in dieser Strategie ist über ein Attribut in seinen CustomData zu identifizieren. Sobald zwei eigene Roboter auf dem Feld sind, bekommt der erste Roboter das Ziel zwischen einem bestimmten Punkt (Abholpunkt) 
     * und dem Spawn hin und her zu pendeln. Dabei nimmt er von diesem Punkt Rohstoffe auf und liefert sie am Spawn ab. 
     * Die Aufgabe der anderen Roboter ist es, den Abholpunkt mit Ressourcen zu versorgen. Dafür bewegen sie sich zufällig über den Spielplan. 
     * Ist ihr Lager voll fahren sie jedoch entsprechend nicht zum Spawn zurück, sondern zum Abholpunkt. Von dort übernimmt dann der erste Roboter.
     * Daher kommt auch der Name dieser Strategie. Sobald mehrere Roboter verfügbar sind fährt kein Roboter mehr die gesamte Strecke ab. 
     * Die Ressourcen werden immer weitergegeben, der Weg somit aufgeteilt. 
     * Allerdings bringt diese Strategie auch einige Nachteile mit sich: 
     * Sind zwei oder mehr Roboter mit dem Füllen des Abholpunktes beschäftigt, besteht die Gefahr, dass der Abhol-Roboter nicht mehr hinterherkommt. 
     * Grund dafür ist, dass das Abladen aller Ressourcen solange dauert wie das Aufladen einer einzigen. Ein weiterer Nachteil ist, dass die Sammel-Roboter ggf. voll am Spawn vorbei fahren um die Ressourcen am Abholpunkt abzuliefern. 
     * Um jedoch alle Richtungen abzudecken wäre ein großer Roboter- und damit Ressourcenaufwand nötig. Zudem besteht das Risiko, dass andere Spieler die am Abholpunkt abgelegten Ressourcen klauen könnten. 
     */
    private static final long serialVersionUID = 1L;
    
    
    public Strategy() throws RemoteException {
        super();
       
    }

    boolean init = false;
    boolean onWayHome = false;
    boolean onWayBack = true;
    
    int lastGoalX = 0;
    int lastGoalY = 0;
    
    int size;
    int plannedAgents = 0;
    int collecterX, collecterY;

    
    //Eimerkette
    @Override
    public void nextAction(IAgent agent) throws RemoteException {    	  	
        agent.take(); //Default Aktion
        
        IAgent[] ar = agent.getAgentArray(); 
        //Id des Roboters festlegen
        if (agent.getCustomData(0, 0) == 0){
        	agent.setCustomData(0, 0, agent.getAgentArray().length);
        }
        
        
        if (!init){
        	init = true;
            size = agent.getRememberFieldSize();
            
            plannedAgents = 3; // Aufrunden
            System.out.println("Geplante Agenten: " + plannedAgents);
                                    
            collecterX = agent.getHomeXY() / 3 * 2;
            collecterY = agent.getHomeXY() - 0;    	
        }
        
        
        if (ar.length == 1){
    		//collecterAction(agent); 
    		//finderAction(agent);
        	singelRobotAction(agent);
        	return;
        
     
        //Bewegen
    	} else if (ar.length >= 2){
        	if (agent.getCustomData(0, 0) == 1){
        		collecterAction(agent);  
        		return;
    		} else {
    			finderAction(agent);
    			return;
    		}
        }
    }
    
    
    



	public void singelRobotAction(IAgent agent){
        if (agent.check() > 0 && (agent.getLoad() < agent.getCapacity())) {
            agent.take();
            return;
        }    
        
        
        if (agent.buyPossible() && agent.getAgentArray().length < plannedAgents){
        	agent.buy();
        	return;
        }
        
        
        if (agent.getLoad() >= agent.getCapacity()){
        	int home = agent.getHomeXY();
        	if ((agent.getPosx() > home+1) && (agent.requestField(AgentUtils.LEFT))){
        		agent.go(AgentUtils.LEFT);
        	} else if ((agent.getPosx() < home-1) && (agent.requestField(AgentUtils.RIGHT))){
        		agent.go(AgentUtils.RIGHT);
        	} else if ((agent.getPosy() > home+1) && (agent.requestField(AgentUtils.TOP))){
        		agent.go(AgentUtils.TOP);
        	} else if ((agent.getPosy() < home-1) && (agent.requestField(AgentUtils.BOTTOM))){
        		agent.go(AgentUtils.BOTTOM);
        	} else if (agent.checkIfOnSpawn()){
        		agent.put();
        	} else {
        		moveRandom(agent);
        	}
        	return;
        }
        
        moveRandom(agent);
    }
    
    
    
    
    
    public void moveRandom(IAgent agent){
    	int direction = 1 + (int)(Math.random() * ((4 - 1) + 1));
    	    	
    	switch (direction){
    	case 0:
    		if (agent.requestField(AgentUtils.LEFT)) {
                agent.go(AgentUtils.LEFT);
            }
    		break;
    	case 1:
    		if (agent.requestField(AgentUtils.TOP)) {
                agent.go(AgentUtils.TOP);
            }
    		break;
    	case 2:
    		if (agent.requestField(AgentUtils.RIGHT)) {
                agent.go(AgentUtils.RIGHT);
            }
    		break;
    	case 3:
    		if (agent.requestField(AgentUtils.BOTTOM)) {
                agent.go(AgentUtils.BOTTOM);
    		}
    		break;
    		
    	default:
   	
            if (agent.requestField(AgentUtils.LEFT)) {
                agent.go(AgentUtils.LEFT);
            } else if (agent.requestField(AgentUtils.TOP)) {
                agent.go(AgentUtils.TOP);
            } else if (agent.requestField(AgentUtils.RIGHT)) {
                agent.go(AgentUtils.RIGHT);
            } else if (agent.requestField(AgentUtils.BOTTOM)) {
                agent.go(AgentUtils.BOTTOM);
            }
    	}    
        
    
    }
    
    
    public void collecterAction(IAgent agent){
    	//System.out.println("Position: " + agent.getPosx() + " / " + agent.getPosy());
    	
    	if (onWayBack) {
    		if (moveTo(agent, collecterX, collecterY)){
    			System.out.println("Am Collector Punkt");
    			onWayBack = false;
    			onWayHome = false;
    			return;
    		}
    	}
    	
    	if (onWayHome){
    		if (moveHome(agent)){
    			System.out.println("Zu Hause");
    			onWayHome = false;
    			onWayBack = true;
    		}    		
    	}
    	
    	if (!onWayHome && !onWayBack) {
    		System.out.println("Von Collector Punkt aufgenommen");
    		agent.take();
    		if (agent.check() == 0 || agent.getLoad() >= agent.getCapacity()){
    			onWayHome = true;
    		}

    		return;
    	}
    }
    
    
    public boolean moveHome(IAgent agent) {
    	int home = agent.getHomeXY();
    	if ((agent.getPosx() > home+1) && (agent.requestField(AgentUtils.LEFT))){
    		agent.go(AgentUtils.LEFT);
    	} else if ((agent.getPosx() < home-1) && (agent.requestField(AgentUtils.RIGHT))){
    		agent.go(AgentUtils.RIGHT);
    	} else if ((agent.getPosy() > home+1) && (agent.requestField(AgentUtils.TOP))){
    		agent.go(AgentUtils.TOP);
    	} else if ((agent.getPosy() < home-1) && (agent.requestField(AgentUtils.BOTTOM))){
    		agent.go(AgentUtils.BOTTOM);
    	} else if (agent.checkIfOnSpawn()){
    		agent.put();
    		return true;
    	}        	    
    	return false;
    }
    
    
    
    private void finderAction(IAgent agent) {   	
        if (agent.check() > 0 && (agent.getLoad() < agent.getCapacity()) && !(agent.getPosx() == collecterX && agent.getPosy() == collecterY)) {
            agent.take();
            return;
        }    
        
        
        if (agent.buyPossible() && agent.getAgentArray().length < plannedAgents){
        	agent.buy();
        	return;
        }
        
        
        if (agent.getLoad() >= agent.getCapacity()){       	
        	if ((agent.getPosx() > collecterX) && (agent.requestField(AgentUtils.LEFT))){
        		agent.go(AgentUtils.LEFT);
        	} else if ((agent.getPosx() < collecterX) && (agent.requestField(AgentUtils.RIGHT))){
        		agent.go(AgentUtils.RIGHT);
        	} else if ((agent.getPosy() > collecterY) && (agent.requestField(AgentUtils.TOP))){
        		agent.go(AgentUtils.TOP);
        	} else if ((agent.getPosy() < collecterY) && (agent.requestField(AgentUtils.BOTTOM))){
        		agent.go(AgentUtils.BOTTOM);
        	} else if (agent.getPosx() == collecterX && agent.getPosy() == collecterY){
        		agent.put();
        	} else {
        		moveRandom(agent);
        	}
        	return;
        }
        
        moveRandom(agent);
		
	}


    
    public boolean moveTo(IAgent agent, int x, int y) {    	
    		System.out.println("Auf dem Weg zu: " + x + "/" + y);
    		
    		if ((agent.getPosy() > y) && (agent.requestField(AgentUtils.TOP))){
        		agent.go(AgentUtils.TOP);
        	} else if ((agent.getPosx() < x) && (agent.requestField(AgentUtils.RIGHT))){
        		agent.go(AgentUtils.RIGHT);
        	} else if ((agent.getPosx() > x) && (agent.requestField(AgentUtils.LEFT))){
        		agent.go(AgentUtils.LEFT);
        	} else if ((agent.getPosy() < y) && (agent.requestField(AgentUtils.BOTTOM))){
        		agent.go(AgentUtils.BOTTOM);
        	} else if (agent.getPosx() == x && agent.getPosy() == y){       		
        		return true;
        	} else {
        		System.out.println("Blockiert, move random");
        		moveRandom(agent);
        	}        	
    		return false;
    }
    

}
