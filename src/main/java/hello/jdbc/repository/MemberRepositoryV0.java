package hello.jdbc.repository;

import hello.jdbc.conection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC - DriverManager 사용
 */
@Slf4j
public class MemberRepositoryV0 {

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

    /**
     * reparedStatement는 Connection을 통해 실행되며, ResultSet은 PreparedStatement에 의해 반환.
     * 열린 순서의 역순으로 닫아야 한다.
     *
     * @param rs   PreparedStatement에 의존.
     * @param stmt Connection 객체에 의존
     * @param con  Connection을 닫으면 데이터베이스와의 연결이 끊어지기 때문에,
     *             연결이 끊어지기 전에 모든 Statement와 ResultSet을 닫아야.
     */
    private void close(ResultSet rs, Statement stmt, Connection con) {

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error", e);
            }

        }
    }

    private static Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
