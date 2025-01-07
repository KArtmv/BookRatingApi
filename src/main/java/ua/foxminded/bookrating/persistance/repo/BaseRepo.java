package ua.foxminded.bookrating.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import ua.foxminded.bookrating.persistance.entity.BaseEntity;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepo<T extends BaseEntity, ID extends Serializable> extends JpaRepository<T, ID> {

    @Modifying
    @Query(value = "update #{#entityName} e set deleted = false where e.id = :id", nativeQuery = true)
    void restore(@Param("id") Long id);


}
