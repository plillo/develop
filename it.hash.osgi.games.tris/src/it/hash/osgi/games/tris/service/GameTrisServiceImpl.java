package it.hash.osgi.games.tris.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.osgi.service.component.annotations.Component;

@Component(immediate=true)
public class GameTrisServiceImpl implements GameTrisService {
	List<String> waitingClientIds = new ArrayList<String>();
	List<String> games = new ArrayList<String>();

	@Override
	public Map<String, Object> match(String clientId) {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		if(waitingClientIds.size()==0) {
			waitingClientIds.add(clientId);
			
			response.put("waitingNumber", waitingClientIds.size());
			// STATUS
			response.put("status", Status.WAITING.getCode());
			response.put("message", Status.WAITING.getMessage());
			
			return response;
		}

		// GET players
		List<String> players = new ArrayList<String>();
		players.add(waitingClientIds.remove(0));
		players.add(clientId);
		response.put("players", players);
		
		// STATUS
		response.put("status", Status.MATCHED.getCode());
		response.put("message", Status.MATCHED.getMessage());

		return response;
	}

}
