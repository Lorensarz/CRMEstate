package com.petrov.content_processor.batch;

import com.petrov.content_processor.dto.EstateDataDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Component
public class EstateItemReader implements ItemReader<EstateDataDto> {

	private final BlockingQueue<EstateDataDto> queue = new LinkedBlockingQueue<>();

	@Override
	public EstateDataDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		try {
			EstateDataDto item = queue.poll();
			if (item != null) {
				log.debug("Reading item from queue: {}", item.getCadastrNumber());
			}
			return item;
		} catch (Exception e) {
			log.error("Error reading from queue", e);
			throw new NonTransientResourceException("Failed to read from queue", e);
		}
	}

	public void addItems(java.util.List<EstateDataDto> items) {
		log.info("Adding {} items to reader queue", items.size());
		queue.addAll(items);
	}

	public int getQueueSize() {
		return queue.size();
	}

	public void clearQueue() {
		queue.clear();
		log.info("Reader queue cleared");
	}
}