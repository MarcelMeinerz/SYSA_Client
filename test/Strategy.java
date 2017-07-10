



import multiagent.remote.IStrategy;
import gameclient.AgentUtils;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import multiagent.remote.IAgent;

public class Strategy extends UnicastRemoteObject implements IStrategy, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    
    public Strategy() throws RemoteException {
        super();
       
    }

    @Override
    public void nextAction(IAgent agent) throws RemoteException {
    	  	
        agent.take(); //Default Aktion
        
        
        agent.setCustomData(0, 1, 2);
		
        
        if(agent.buyPossible()){
        	agent.buy();
        	return;
        }
        if (agent.check() > 0) {
            agent.take();
        } else {

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
        	}        	        	     	        	
        }
        
    }
    


    

}
