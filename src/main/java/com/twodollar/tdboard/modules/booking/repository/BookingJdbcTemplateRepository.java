package com.twodollar.tdboard.modules.booking.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BookingJdbcTemplateRepository {
    private JdbcTemplate jdbcTemplate;

    public BookingJdbcTemplateRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // TODO month..
    public List<Map<String,String>> bookingStatus(String bookingType) {
        // 간단한 SELECT 쿼리 실행 예시
        String query = "SELECT f.name \"강의실\", date_format(b.start_at, '%Y-%m-%d') \"시작일\" , date_format(b.start_at, '%H:%i') \"시작시\", date_format(b.end_at, '%Y-%m-%d') \"종료일\", date_format(b.end_at, '%H:%i') \"종료시\" \n" +
                "FROM booking b\n" +
                "LEFT OUTER JOIN facility f ON b.facility_id = f.id\n" +
                "WHERE b.booking_type ='" + bookingType +"'\n" +
                "AND b.approval_yn = 'Y'\n" +
                "ORDER BY date_format(b.start_at, '%Y-%m-%d'), b.start_at, b.end_at";
        List<Map<String,String>> list = jdbcTemplate.query(query, new RowMapper<Map<String,String>>() {
            @Override
            public Map<String,String> mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map<String,String> row = new LinkedHashMap<>();
                row.put("강의실", rs.getString("강의실"));
                row.put("시작일", rs.getString("시작일"));
                row.put("시작시", rs.getString("시작시"));
                row.put("종료일", rs.getString("종료일"));
                row.put("종료시", rs.getString("종료시"));
                return row;
            }
        });

        return list;
    }


}
