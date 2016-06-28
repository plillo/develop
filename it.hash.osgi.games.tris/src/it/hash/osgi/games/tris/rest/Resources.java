package it.hash.osgi.games.tris.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.annotations.Api;
import it.hash.osgi.broker.BrokerService;
import it.hash.osgi.games.tris.service.GameTrisService;
import it.hash.osgi.games.tris.service.Status;
import it.hash.osgi.utils.Random;


@Api
@Path("games/1.0/tris/")
@Component(service = Resources.class)
public class Resources {
	// References
	// ==========
	private GameTrisService _gameService;
	private BrokerService _brokerService;
	
	@Reference(service=GameTrisService.class)
	public void setGameTrisService(GameTrisService service){
		_gameService = service;
		doLog("GameTrisService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetGameTrisService(GameTrisService service){
		doLog("GameTrisService: "+(service==null?"NULL":"released"));
		_gameService = null;
	}
	
	@Reference(service=BrokerService.class)
	public void setBrokerService(BrokerService service){
		_brokerService = service;
		doLog("BrokerService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetBrokerService(BrokerService service){
		doLog("BrokerService: "+(service==null?"NULL":"released"));
		_brokerService = null;
	}
	// === end references
	
	// API
	// ===
	// POST games/1.0/tris/player/{clientid}/subscribe
	@POST
	@Path("player/{clientid}/subscribe")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    @io.swagger.annotations.ApiOperation(value = "subscribe", notes = "...")
	public Response subscribe(@PathParam("clientid") PathSegment clientid) {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		response = _gameService.match(clientid.getPath());
		if((int)response.get("status")==Status.MATCHED.getCode()){
			// Generate a GAME SESSION ID
			String gameSessionId = Random.getRandomKey(10);
			String gameSessionTopic = "games/tris/"+gameSessionId;
			
			@SuppressWarnings("unchecked")
			List<String> players = (List<String>)response.get("players");
			// Send "ready" command and player ID to all players
			int counter = 0;
			for(String player : players) {
				String playerTopic = "games/tris/"+player;
				// >> playerTopic (deliverd to a single player)
				_brokerService.publish(playerTopic, 2, "{\"command\":\"ready\",\"gameSessionTopic\":\""+gameSessionTopic+"\",\"playerId\":"+(++counter)+"}");
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// >> gameSessionTopic (deliverd to all players): send "setup" command
			_brokerService.publish(gameSessionTopic, 2, "{\"command\":\"setup\",\"playersNumber\":"+players.size()+"}");
			
			// >> gameSessionTopic (deliverd to all players): send "start" command and starter player ID
			_brokerService.publish(gameSessionTopic, 2, "{\"command\":\"start\",\"playerStarter\":1}");
		}
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}
	
	// API
	// ===
	// PUT games/1.0/tris/player/{clientid}/action
	@POST
	@Path("player/{clientid}/action")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    @io.swagger.annotations.ApiOperation(value = "subscribe", notes = "...")
	public Response action(@PathParam("clientid") PathSegment clientid, HashMap<String, Object> data /*TreeMap<String, Object> data*/) {
		Map<String, Object> response = new TreeMap<String, Object>();

		String gameSessionTopic = (String) data.get("gameSessionTopic");
		Double playerId = (Double)data.get("playerId");
		List<List<Double>> state = (List<List<Double>>) data.get("state");
		List<Double> timers = (List<Double>) data.get("timers");
		int nextPlayerId = playerId==1?2:1;
		
		int r=0;
		double[][] stateArray = new double[3][3];
		for(Iterator<List<Double>> itr=state.iterator();itr.hasNext();r+=1){
			int c = 0;
			for(Iterator<Double> itc=itr.next().iterator();itc.hasNext();c+=1){
				stateArray[r][c] = itc.next();
			}
		}
		
		List<Integer> tris = new ArrayList<Integer>();
		tris.add(0);
		tris.add(0);
		tris.add(0);
		int winner = 0;
		if(winner(stateArray, tris, 1))
			winner = 1;
		else if(winner(stateArray, tris, 2))
			winner = 2;
		
		String command = "\"play\"";
		if(winner>0 || full(stateArray))
			command = "\"finish\"";
		
		if(winner==0 && full(stateArray)){
			tris.add(2, 3);
			if(timers.get(0)<timers.get(1))
				winner = 1;
			else if(timers.get(0)>timers.get(1))
				winner = 2;
			else winner = -1;
		}
	
		String winnerMsg = winner>0?",\"winner\":"+winner+",\"tris\":"+tris.toString():"";
		
		String msg = "{\"command\":"+command+winnerMsg+",\"state\":"+state.toString()+",\"timers\":"+timers.toString()+",\"nextPlayer\":"+nextPlayerId+"}";
		
		// >> gameSessionTopic (deliverd to all players): send "start" command and starter player ID
		_brokerService.publish(gameSessionTopic, 2, msg);
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").build();
	}
	
	private boolean full(double[][] stateArray){
		for(int k=0;k<3;k++)
			for(int m=0;m<3;m++)
				if(stateArray[k][m]==0)
					return false;
	
		return true;
	}
	
	private boolean winner(double[][] stateArray, List<Integer> tris, int w){
		int winner = 0;
		ext:
		for(int k=0;k<3;k++){
			for(int m=0;m<3;m++){
				if(stateArray[k][m]!=w)
					continue ext;
			}
			winner = w;
			tris.set(0, k+1);
			break;
		}
		if(winner==0)
			ext:
			for(int k=0;k<3;k++){
				for(int m=0;m<3;m++){
					if(stateArray[m][k]!=w){
						continue ext;
					}
				}
				winner = w;
				tris.set(1, k+1);
				break;
			}
		if(winner==0)
			for(int k=0;k<3;k++){
				if(stateArray[k][k]!=w){
					break;
				}
				if(k==2){
					winner = w;
					tris.set(2, 1);
				}
			}	
		if(winner==0)
			for(int k=0;k<3;k++){
				if(stateArray[k][2-k]!=w){
					break;
				}
				if(k==2){
					winner = w;
					tris.set(2, 2);
				}
			}	
		
		return winner==w;
	}
	
    private void doLog(String message) {
        System.out.println("## [" + this.getClass() + "] " + message);
    }
}
