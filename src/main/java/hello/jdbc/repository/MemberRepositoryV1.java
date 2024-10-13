package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC - DataSource, JdbcUtils 사용
 */
@Slf4j
public class MemberRepositoryV1 {

    private final DataSource dataSource;

    public MemberRepositoryV1(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values (?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;


        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            //executeUpdate()는 데이터베이스에 변경을 가하는 SQL 쿼리(예: INSERT, UPDATE, DELETE)를 실행할 때 사용
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(null, pstmt, con); //rs는 db 읽기에 사용. 현재는 쓰기 중임으로 필요 없음.
        }
    }

    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found : memberId= " + memberId);
            }

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(rs, pstmt, con);
        }
    }

    public void update(String memberId, int money) throws SQLException {

        String sql = "update member set money=? where member_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);

            int resultSize = pstmt.executeUpdate();
            log.info("resultSize= {}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(null, pstmt, con);
        }
    }

    public void delete(String meberId) throws SQLException {
        String sql = "delete from member where member_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, meberId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize= {}", resultSize);

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(null, pstmt, con);
        }
    }

    /**
     * JdbcUtils.closeXXX(xxx) 사용.
     */
    private void close(ResultSet rs, Statement stmt, Connection con) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }

    /**
     * **JdbcUtils.closeConnection(con)**은 커넥션이 커넥션 풀의 것인지, 직접 생성된 것인지를 자동으로 판단하고,
     * 각각에 맞는 방식으로 처리합니다.
     * 커넥션 풀의 경우: 커넥션을 풀로 반환.
     * 직접 생성된 커넥션의 경우: 커넥션을 실제로 닫음.
     * 따라서 close() 메서드를 호출하는 코드는 변경할 필요 없이, 커넥션 풀을 사용하든 그렇지 않든 안전하게 동작합니다.
     */
    private Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("get connection= {}, class= {}", con, con.getClass());
        return con;
    }
}
