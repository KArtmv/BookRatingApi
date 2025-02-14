package ua.foxminded.bookrating.service.implementation;

import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.bookrating.dto.PublisherDto;
import ua.foxminded.bookrating.persistance.entity.Publisher;
import ua.foxminded.bookrating.persistance.repo.PublisherRepository;
import ua.foxminded.bookrating.service.PublisherService;

@Service
@Transactional(readOnly = true)
public class PublisherServiceImpl extends ExtendedCrudServiceImpl<Publisher, PublisherDto> implements PublisherService {

    public PublisherServiceImpl(PublisherRepository repository) {
        super(repository, Publisher::new);
    }

    @Override
    public Publisher create(PublisherDto dto) {
        if (findByName(dto.name()).isPresent()) {
            throw new EntityExistsException("Publisher with given name: " + dto.name() + ", already exists");
        }
        return super.save(new Publisher(dto.name()));
    }

    @Override
    public Publisher update(Long id, PublisherDto dto) {
        Publisher publisher = findById(id);
        publisher.setName(dto.name());
        return super.save(publisher);
    }
}
