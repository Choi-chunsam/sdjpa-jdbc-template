package guru.springframework.jdbc;


import guru.springframework.jdbc.dao.AuthorDao;
import guru.springframework.jdbc.dao.AuthorDaoImpl;
import guru.springframework.jdbc.dao.BookDao;
import guru.springframework.jdbc.domain.Author;
import guru.springframework.jdbc.domain.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@ComponentScan(basePackages = "guru.springframework.jdbc.dao")
public class AuthorIntegrationTest {

    @Autowired
    AuthorDao authorDao;

    @Autowired
    BookDao bookDao;

    @Test
    void testGetBookById(){
        Book book = bookDao.getById(1L);

        assertThat(book.getId()).isNotNull();
    }

    @Test
    void testGetBookByTitle(){

        Book book = bookDao.findByTitle("Clean Code");

        assertThat(book).isNotNull();
    }

    @Test
    void testInsertBook(){
        Book book = new Book();
        book.setPublisher("choi");
        book.setTitle("jsp");
        book.setIsbn("askdjl");

        Book saved = bookDao.saveNewBook(book);

        assertThat(saved).isNotNull();
    }

    @Test
    void testUpdateBook(){
        Book book = new Book();
        book.setPublisher("choi");
        book.setTitle("jsp");
        book.setIsbn("asjdkasd");

        Book saved = bookDao.saveNewBook(book);
        saved.setTitle("servlet");

        Book updated = bookDao.updateBook(saved);

        assertThat(updated.getTitle()).isEqualTo("servlet");

    }

    @Test
    void testDeleteBook(){
        Book book = new Book();
        book.setPublisher("choi");

        Book saved = bookDao.saveNewBook(book);

        bookDao.deleteBookById(saved.getId());

        assertThrows(EmptyResultDataAccessException.class,()->{
            authorDao.getById(saved.getId());
        });
    }

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

        assertThrows(TransientDataAccessResourceException.class,() -> {
            authorDao.getById(saved.getId());
        });

        //assertThat(deleted).isNull();
    }

    @Test
    void testGetAuthot(){
        Author author = authorDao.getById(1L);

        assertThat(author.getId()).isNotNull();
    }
}
