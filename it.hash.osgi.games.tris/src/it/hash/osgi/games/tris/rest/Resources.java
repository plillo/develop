package it.hash.osgi.games.tris.rest;

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
				_brokerService.publish(playerTopic, "{\"command\":\"ready\",\"gameSessionTopic\":\""+gameSessionTopic+"\",\"playerId\":"+(++counter)+"}");
			}
			
			// >> gameSessionTopic (deliverd to all players): send "setup" command
			_brokerService.publish(gameSessionTopic, "{\"command\":\"setup\",\"playersNumber\":"+players.size()+"}");
			
			// >> gameSessionTopic (deliverd to all players): send "start" command and starter player ID
			_brokerService.publish(gameSessionTopic, "{\"command\":\"start\",\"playerStarter\":1}");
		}
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}
	
    private void doLog(String message) {
        System.out.println("## [" + this.getClass() + "] " + message);
    }
}
