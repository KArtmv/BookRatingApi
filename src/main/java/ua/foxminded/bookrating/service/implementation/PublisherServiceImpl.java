package ua.foxminded.bookrating.service.implementation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ua.foxminded.bookrating.persistance.entity.Publisher;
import ua.foxminded.bookrating.service.PublisherService;

import java.util.Set;

@Service
public class PublisherServiceImpl extends AbstractServiceImpl<Publisher> implements PublisherService {

    public PublisherServiceImpl(JpaRepository<Publisher, Long> repository) {
        super(repository);
    }
}
