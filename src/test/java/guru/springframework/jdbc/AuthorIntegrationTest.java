package guru.springframework.jdbc;


import guru.springframework.jdbc.dao.AuthorDao;
import guru.springframework.jdbc.dao.AuthorDaoImpl;
import guru.springframework.jdbc.domain.Author;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Import(AuthorDaoImpl.class)
public class AuthorIntegrationTest {

    @Autowired
    AuthorDao authorDao;

    @Test
    void testGetAuthorById(){
        Author author = authorDao.getById(1L);

        assertThat(author.getId()).isNotNull();
    }

    @Test
    void testFindAuthorByName(){
        Author author = authorDao.findAuthorByName("Craig","Walls");
        assertThat(author).isNotNull();
    }

    @Test
    void testInsertAuthor(){
        Author author = new Author();
        author.setFirstName("Choi");
        author.setLastName("Daewoong");

        Author saved = authorDao.saveNewAuthor(author);

        assertThat(saved).isNotNull();
    }

    @Test
    void testUpdateAuthor(){
        Author author = new Author();
        author.setFirstName("Choi");
        author.setLastName("sibal");

        Author saved = authorDao.saveNewAuthor(author);
        saved.setLastName("Daewoong");

        Author updated = authorDao.updateAuthor(saved);

        assertThat(updated.getLastName()).isEqualTo("Daewoong");

    }

    @Test
    void testDeleteAuthor(){
        Author author = new Author();
        author.setFirstName("Choi");
        author.setLastName("Daewoong");

        Author saved = authorDao.saveNewAuthor(author);

        authorDao.deleteAuthorById(saved.getId());

        assertThrows(EmptyResultDataAccessException.class,() -> {
            authorDao.getById(saved.getId());
        });

        //assertThat(deleted).isNull();
    }
}
