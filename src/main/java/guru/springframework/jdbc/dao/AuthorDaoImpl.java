package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AuthorDaoImpl implements AuthorDao {
    //spring context에 요청해서 autowired했고
    private final JdbcTemplate jdbcTemplate;

    public AuthorDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Author getById(Long id) {
        String sql = "select author.id as id, first_name, last_name, book.id as book_id, book.isbn, book.publisher, book.title from author\n" +
                "left outer join book on author.id = book.author_id where author.id = ?";

        //queryForObject는 오버로드된 것이다 우리가 다룰 수 있는 방법이 다양하다.
        // 나는 RowMapper를 넘기고 그런 다음 이 경우 바인드 매개변수 목록을 가져옵니다.
        return jdbcTemplate.query(sql,new AuthorExtractor(),id);
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        return jdbcTemplate.queryForObject("select * from author where first_name=? and last_name=?",getRowMapper(),firstName,lastName);
    }

    @Override
    public Author saveNewAuthor(Author author) {
        jdbcTemplate.update("INSERT INTO author (first_name,last_name) VALUES (?,?)",
                            author.getFirstName(),author.getLastName());

        Long createdId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()",Long.class);//데이터 타입 넣어줌

        return this.getById(createdId);
    }

    @Override
    public Author updateAuthor(Author author) {
        jdbcTemplate.update("UPDATE author SET first_name = ?, last_name = ? WHERE id = ?",
                             author.getFirstName(), author.getLastName(),author.getId());

        return this.getById(author.getId());
    }

    @Override
    public void deleteAuthorById(Long id) {
        jdbcTemplate.update("DELETE FROM author WHERE id = ?",id);
    }

    private RowMapper<Author> getRowMapper(){
        return new AuthorMapper();
    }


}