package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import guru.springframework.jdbc.domain.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class BookDaoImpl implements BookDao{

    private final JdbcTemplate jdbcTemplate;

    public BookDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Book findByTitle(String title) {
        return jdbcTemplate.queryForObject("select * from book where title = ?", getRowMapper(),title);
    }

    @Override
    public Book getById(Long id) {

        return jdbcTemplate.queryForObject("select * from book where id = ?",getRowMapper(),id);
    }

    @Override
    public void deleteBookById(Long id) {
        jdbcTemplate.update("DELETE FROM book WHERE id = ?",id);
    }

    @Override
    public Book updateBook(Book book) {
        jdbcTemplate.update("UPDATE book SET title=?,publisher=?,isbn=?,author_id=?WHERE id=?",
                            book.getTitle(),book.getPublisher(),book.getIsbn(),book.getAuthorId(),book.getId());

        return this.getById(book.getId());
    }

    @Override
    public Book saveNewBook(Book book) {
         jdbcTemplate.update("INSERT INTO book (title,publisher,isbn,author_id) VALUES (?,?,?,?)",
                book.getTitle(),book.getPublisher(),book.getIsbn(),book.getAuthorId());
        Long createdId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()",Long.class);
         return this.getById(createdId);
    }

    public RowMapper<Book> getRowMapper(){
        return new BookMapper();
    }
}
