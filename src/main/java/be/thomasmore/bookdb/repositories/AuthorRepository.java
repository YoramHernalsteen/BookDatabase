package be.thomasmore.bookdb.repositories;

import be.thomasmore.bookdb.model.Author;
import be.thomasmore.bookdb.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AuthorRepository extends CrudRepository<Author, Integer> {
    public Optional<Author>findByNameLike(String authorName);
    @Query("select a from Author a where" +
            "(:author is null or  a.name like %:author%)"
    )
    public Iterable<Author>name(@Param("author") String author);
}

