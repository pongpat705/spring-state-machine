package com.maoz.app;

import java.util.EnumSet;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;


@Configuration
@EnableStateMachineFactory
public class BookStateMachineConfig extends EnumStateMachineConfigurerAdapter<BookStates, BookEvents> {

	private BookStateListener stateListener;
	private StatePersist statePersist;

	public BookStateMachineConfig(BookStateListener stateListener, StatePersist statePersist) {
		this.stateListener = stateListener;
		this.statePersist = statePersist;
	}

	@Override
	public void configure(StateMachineConfigurationConfigurer<BookStates, BookEvents> config) throws Exception {
		config.withConfiguration()
			.autoStartup(true)
			.listener(stateListener)
			.machineId("bookFlow")
			;
	}

	@Override
	public void configure(StateMachineStateConfigurer<BookStates, BookEvents> states) throws Exception {
		states.withStates()
			.initial(BookStates.AVAILABLE)
			.states(EnumSet.allOf(BookStates.class))
		;
	}

	@Override
	public void configure(StateMachineTransitionConfigurer<BookStates, BookEvents> transitions) throws Exception {
		transitions
	        .withExternal()
	        .source(BookStates.AVAILABLE)
	        .target(BookStates.BORROWED)
	        .event(BookEvents.BORROW)
	        .and()
	        .withExternal()
	        .source(BookStates.BORROWED)
	        .target(BookStates.AVAILABLE)
	        .event(BookEvents.RETURN)
	        .and()
	        .withExternal()
	        .source(BookStates.AVAILABLE)
	        .target(BookStates.IN_REPAIR)
	        .event(BookEvents.START_REPAIR)
	        .and()
	        .withExternal()
	        .source(BookStates.IN_REPAIR)
	        .target(BookStates.AVAILABLE)
	        .event(BookEvents.END_REPAIR)
        ;
	}

	@Bean
	public StateMachinePersist<BookStates, BookEvents, UUID> inMemoryPersist() {
	    return this.statePersist;
	}

	@Bean
	public StateMachinePersister<BookStates, BookEvents, UUID> persister(
			@Qualifier("statePersist") StateMachinePersist<BookStates, BookEvents, UUID> defaultPersist) {

	    return new DefaultStateMachinePersister<>(defaultPersist);
	}

}
