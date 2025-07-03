package com.tpqms2061.dashboard.repository;

import com.tpqms2061.dashboard.model.CustomerRank;
import com.tpqms2061.dashboard.model.DailySales;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Repository
public class DashboardRepository {
    private final JdbcTemplate jdbcTemplate;

    public DashboardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<DailySales> dailySalesRowMapper = (resultSet, rowNum) ->
            new DailySales(
                    resultSet.getDate("sale_date").toLocalDate(), //날짜데이트라서 toLocalDate()
                    resultSet.getBigDecimal("total_amount"),
                    resultSet.getInt("order_count")
                    //columnLabel은 db에 생성될 열의 이름
            );

    private final RowMapper<CustomerRank> customerRankRowMapper = (resultSet, rowNum) ->
            new CustomerRank(
                    resultSet.getInt("customer_id"),
                    resultSet.getBigDecimal("total_spent"),
                    resultSet.getInt("rank")
            );


    public  List<CustomerRank> findCustomerRankings() {
        String sql = """
                SELECT customer_id,
                       total_spent,
                       RANK() OVER (ORDER BY total_spent DESC) AS rank
                FROM customer_spending
                """;

        return jdbcTemplate.query(sql, customerRankRowMapper);
    }
//        customer id, total_spent, 를 가져오는데 이걸 total_spent 를 내림차순으로 랭크를 매기며 이걸 rank라고 부르기로 함


    public List<DailySales> findDailySales() {
        return jdbcTemplate.query(
                "SELECT * FROM daily_sales",
                dailySalesRowMapper
        );
    }




    //LocalDate 시간 객체 / BigDecmal 돈단위가 크기때문에 int 대신
    public void saveSale(LocalDate saleData, int customerId , BigDecimal amount) {
        jdbcTemplate.update(
                "INSERT INTO sales (sale_date, customer_id, amount) VALUES (?, ?, ?)",
                // db에서 만들어준 데이터를 작성해야됨/ 자바 파라미터가 아님
                saleData, customerId, amount // (?,?,?) 에 해당되는 매개변수
        );
    }
}
