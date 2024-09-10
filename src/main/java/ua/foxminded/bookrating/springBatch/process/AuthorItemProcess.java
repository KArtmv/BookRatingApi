package ua.foxminded.bookrating.springBatch.process;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ua.foxminded.bookrating.dto.AuthorCsvDto;
import ua.foxminded.bookrating.persistance.entity.Author;

import java.util.HashSet;
import java.util.Set;

@Component
public class AuthorItemProcess implements ItemProcessor<AuthorCsvDto, Author>, StepExecutionListener {

    private Set<String> authorsCache;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        authorsCache = new HashSet<>();
    }

    @Override
    public Author process(AuthorCsvDto item) {
        String author = replaceAmpersand(item.author().trim());

        if (!authorsCache.contains(author)) {
            authorsCache.add(author);
            return new Author(author);
        } else {
            return null;
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        authorsCache.clear();
        return ExitStatus.COMPLETED;
    }

    private String replaceAmpersand(String author) {
        return author.replace("&amp;", "&");
    }
}
