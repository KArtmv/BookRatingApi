package ua.foxminded.bookrating.springBatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@DependsOn("flyway")
@RequiredArgsConstructor
public class SpringBatchJobRunner {

    @Bean
    public Job job(Step ratingStep, Flow startFlow, JobRepository jobRepository) {
        return new JobBuilder("importData", jobRepository)
                .start(startFlow)
                .next(ratingStep)
                .build()
                .build();
    }

    @Bean
    public Flow startFlow(Flow authorPublisherBookFlow, Flow userFlow) {
        return new FlowBuilder<Flow>("authorPublisherParallelFlow")
                .split(taskExecutor())
                .add(authorPublisherBookFlow, userFlow)
                .build();
    }

    @Bean
    public Flow authorPublisherBookFlow(Flow authorFlow, Flow publisherFlow, Step bookStep) {
        return new FlowBuilder<Flow>("authorPublisherBookFlow")
                .split(taskExecutor())
                .add(authorFlow, publisherFlow)
                .next(bookStep)
                .build();
    }

    @Bean
    public Flow authorFlow(Step authorStep) {
        return new FlowBuilder<Flow>("authorFlow")
                .start(authorStep)
                .build();
    }

    @Bean
    public Flow publisherFlow(Step publisherStep) {
        return new FlowBuilder<Flow>("publisherFlow")
                .start(publisherStep)
                .build();
    }

    @Bean
    public Flow userFlow(Step userStep) {
        return new FlowBuilder<Flow>("userFlow")
                .start(userStep)
                .build();
    }

    private SimpleAsyncTaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }
}
