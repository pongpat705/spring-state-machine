package com.maoz.app;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.statemachine.support.DefaultExtendedState;
import org.springframework.statemachine.support.DefaultStateContext;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StatePersist implements StateMachinePersist<BookStates, BookEvents, UUID>{

	private BookRepository bookRepository;
	private BookFlowRepository bookFlowRepository;

	public StatePersist(BookRepository bookRepository, BookFlowRepository bookFlowRepository) {
		this.bookRepository = bookRepository;
		this.bookFlowRepository = bookFlowRepository;
	}

	private HashMap<UUID, StateMachineContext<BookStates, BookEvents>> storage = new HashMap<>();

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Transactional
	@Override
	public void write(StateMachineContext<BookStates, BookEvents> context, UUID contextObj) throws Exception {

		log.info("write {}", contextObj);
		log.info("context {}", context);
//		storage.put(contextObj, context);

		ObjectMapper mapper = new ObjectMapper();
		String xData = mapper.writeValueAsString(context);
		Book book = bookRepository.getOne(contextObj.toString());
		if(null == book) {
			book = new Book();
			setBookState(context, contextObj, xData, book);
		} else {
			setBookState(context, contextObj, xData, book);
		}


		BookFlow bookFlow = new BookFlow();
		bookFlow.setTicketId(contextObj.toString());
		bookFlow.setState(context.getState().name());
		bookFlow.setCreatedDate(new Date());
		log.info("bookFlow {}", bookFlow);
		bookFlowRepository.save(bookFlow);
	}

	private void setBookState(StateMachineContext<BookStates, BookEvents> context, UUID contextObj, String xData, Book book) {
		book.setTicketId(contextObj.toString());
		book.setState(context.getState().name());
		book.setEvent(null != context.getEvent() ? context.getEvent().name() : null);
		book.setCreatedDate(new Date());
		book.setData(xData);
		log.info("book {}", book);
		bookRepository.save(book);
	}

	@Transactional
	@Override
	public StateMachineContext<BookStates, BookEvents> read(UUID contextObj) throws Exception {
		StateMachineContext<BookStates, BookEvents> context = null;
		Book book = bookRepository.getOne(contextObj.toString());
		if(null != book) {
			log.info("read {}", contextObj);
//			context = storage.get(contextObj);
			context = new DefaultStateMachineContext<BookStates, BookEvents>(BookStates.valueOf(book.getState()), null != book.getEvent() ? BookEvents.valueOf(book.getEvent()) : null, null, new DefaultExtendedState(), new HashMap<>());
			log.info("context {}", context);
		}


		return context;
	}
	

}
