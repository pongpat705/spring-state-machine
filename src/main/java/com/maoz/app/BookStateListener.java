package com.maoz.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

@Component
public class BookStateListener implements StateMachineListener<BookStates, BookEvents>{

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void stateChanged(State<BookStates, BookEvents> from, State<BookStates, BookEvents> to) {
		// TODO Auto-generated method stub
		if(null != from && null != to) log.info("State changed from {} to {}", from.getId(), to.getId());
	}

	@Override
	public void stateEntered(State<BookStates, BookEvents> state) {
		log.info("Entered state {}", state.getId());
		
	}

	@Override
	public void stateExited(State<BookStates, BookEvents> state) {
		log.info("Exited state {}", state.getId());
	}

	@Override
	public void eventNotAccepted(Message<BookEvents> event) {
		log.error("Event not accepted: {}", event.getPayload());
	}

	@Override
	public void transition(Transition<BookStates, BookEvents> transition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void transitionStarted(Transition<BookStates, BookEvents> transition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void transitionEnded(Transition<BookStates, BookEvents> transition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stateMachineStarted(StateMachine<BookStates, BookEvents> stateMachine) {
		log.info("Machine started: {}", stateMachine);
	}

	@Override
	public void stateMachineStopped(StateMachine<BookStates, BookEvents> stateMachine) {
		log.info("Machine stopped: {}", stateMachine);
	}

	@Override
	public void stateMachineError(StateMachine<BookStates, BookEvents> stateMachine, Exception exception) {
		log.error("Machine error: {}", stateMachine);
	}

	@Override
	public void extendedStateChanged(Object key, Object value) {
		log.info("Extended state changed: [{}: {}]", key, value);
	}

	@Override
	public void stateContext(StateContext<BookStates, BookEvents> stateContext) {
		// TODO Auto-generated method stub
		
	}

}
