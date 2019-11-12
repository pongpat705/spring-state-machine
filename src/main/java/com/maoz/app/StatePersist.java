package com.maoz.app;

import java.util.HashMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;

import com.fasterxml.jackson.databind.ObjectMapper;

public class StatePersist implements StateMachinePersist<BookStates, BookEvents, UUID>{

	private HashMap<UUID, StateMachineContext<BookStates, BookEvents>> storage = new HashMap<>(); 
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void write(StateMachineContext<BookStates, BookEvents> context, UUID contextObj) throws Exception {
		log.info("write {}", contextObj);
		log.info("history {}", context.getHistoryStates());
		storage.put(contextObj, context);
	}

	@Override
	public StateMachineContext<BookStates, BookEvents> read(UUID contextObj) throws Exception {
		log.info("read {}", contextObj);
		StateMachineContext<BookStates, BookEvents> context = storage.get(contextObj);
		log.info("history {}", context.getHistoryStates());
		return context;
	}
	

}
