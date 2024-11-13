package ua.foxminded.bookrating.service.implementation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.bookrating.persistance.entity.Publisher;
import ua.foxminded.bookrating.persistance.repo.PublisherRepository;
import ua.foxminded.bookrating.service.PublisherService;

@Service
@Transactional(readOnly = true)
public class PublisherServiceImpl extends ExtendedCrudServiceImpl<Publisher> implements PublisherService {

    public PublisherServiceImpl(PublisherRepository repository) {
        super(repository);
    }
}
