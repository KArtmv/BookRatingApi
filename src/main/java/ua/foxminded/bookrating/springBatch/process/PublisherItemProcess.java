package ua.foxminded.bookrating.springBatch.process;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ua.foxminded.bookrating.dto.PublisherCsvDto;
import ua.foxminded.bookrating.persistance.entity.Publisher;

import java.util.HashSet;
import java.util.Set;

@Component
public class PublisherItemProcess implements ItemProcessor<PublisherCsvDto, Publisher>, StepExecutionListener {

    private Set<String> publishersCache;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        publishersCache = new HashSet<>();
    }

    @Override
    public Publisher process(PublisherCsvDto item) {
        String publisher = replaceAmpersand(item.publisher());

        if (!publishersCache.contains(publisher)) {
            publishersCache.add(publisher);
            return new Publisher(publisher);
        } else {
            return null;
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        publishersCache.clear();
        return ExitStatus.COMPLETED;
    }

    private String replaceAmpersand(String author) {
        return author.replace("&amp;", "&");
    }
}
