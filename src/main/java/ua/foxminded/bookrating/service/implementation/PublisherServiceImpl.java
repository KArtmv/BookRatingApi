package ua.foxminded.bookrating.service.implementation;

import jakarta.persistence.EntityExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.bookrating.persistance.entity.Publisher;
import ua.foxminded.bookrating.persistance.repo.PublisherRepository;
import ua.foxminded.bookrating.projection.BookRatingProjection;
import ua.foxminded.bookrating.service.PublisherService;

@Service
@Transactional(readOnly = true)
public class PublisherServiceImpl extends BaseServiceImpl<Publisher> implements PublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherServiceImpl(PublisherRepository repository) {
        super(repository);
        this.publisherRepository = repository;
    }

    @Override
    public Page<BookRatingProjection> getAllBooksById(Long id, Integer desiredAverageRating, Pageable pageable) {
        return publisherRepository.getBooksByPublisher(findById(id), desiredAverageRating, pageable);
    }


    @Override
    @Transactional
    public Publisher save(Publisher publisher) {
        if (!super.existsByName(publisher.getName())) {
            return publisherRepository.save(publisher);
        }
        throw new EntityExistsException("Publisher with name " + publisher.getName() + " already exists");
    }

    @Override
    @Transactional
    public Publisher update(Long id, Publisher newPublisher) {
        Publisher publisher = findById(id);
        publisher.setName(newPublisher.getName());
        return publisherRepository.save(publisher);
    }
}
