package com.maoz.app;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppRestController {

	
	
	@Autowired
	private StateMachineFactory<BookStates, BookEvents> stateMachineFactory;

	@Autowired
	private StateMachinePersister<BookStates, BookEvents, UUID> persister;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping("/workFlow/{state}")
	public Map<String, Object> workFlow(@PathVariable String state, @RequestParam(required = false) UUID ticket) throws Exception {
		log.info("##################################################");
		Map<String, Object> map = new HashMap<String, Object>();
		Boolean accepted = false;
		StateMachine<BookStates, BookEvents> stateMachine = null;
		
		log.info("ticket id {}", ticket);
		
		switch (state) {
		case "START":
			stateMachine = stateMachineFactory.getStateMachine();
			log.info("new ticket id {}", stateMachine.getUuid());
			stateMachine.start();
			accepted = true;
			persister.persist(stateMachine, stateMachine.getUuid());
			break;
		case "BORROW":
			stateMachine = stateMachineFactory.getStateMachine(ticket);
			persister.restore(stateMachine, ticket);
			accepted = stateMachine.sendEvent(BookEvents.BORROW);
			persister.persist(stateMachine, ticket);
			break;
		case "RETURN":
			stateMachine = stateMachineFactory.getStateMachine(ticket);
			persister.restore(stateMachine, ticket);
			accepted = stateMachine.sendEvent(BookEvents.RETURN);
			persister.persist(stateMachine, ticket);
			break;
		case "CURRENT":
			stateMachine = stateMachineFactory.getStateMachine(ticket);
			persister.restore(stateMachine, ticket);
			break;
		default:
			stateMachine = stateMachineFactory.getStateMachine(ticket);
			stateMachine.stop();
			break;
		}
		map.put("isComplete", accepted);
		map.put("ticketId", stateMachine.getUuid());
		log.info("##################################################");
		
		return map;
	}
	
}
