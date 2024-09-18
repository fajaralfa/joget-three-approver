package com.fajaralfa.threeapprover;

import org.joget.apps.app.service.AppUtil;
import org.joget.commons.util.LogUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ThreeApproverMapperDAO {
    public HashMap<String, String> getThreeApprovers(String requesterUsername) {
        DataSource dataSource = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");

        HashMap<String, String> result = new HashMap<>();

        try (Connection con = dataSource.getConnection()) {
            String sql = "SELECT c_atasanLangsung, c_pemimpinGrup, c_staffItSecurity FROM app_fd_data_approval WHERE c_requester = ? LIMIT 1";
            PreparedStatement statement = con.prepareStatement(sql);

            statement.setString(1, requesterUsername);
            ResultSet resultSet = statement.executeQuery();

            int rowCount = 0;
            while (resultSet.next()) {
                result.put("approverAtasanLangsung", resultSet.getString("c_atasanLangsung"));
                result.put("approverPemimpinGrupCabang", resultSet.getString("c_pemimpinGrup"));
                result.put("approverStaffITSecurity", resultSet.getString("c_staffItSecurity"));

                rowCount += 1;
            }

            if (rowCount == 0) {
                result.put("approverAtasanLangsung", resultSet.getString("c_atasanLangsung"));
                result.put("approverPemimpinGrupCabang", resultSet.getString("c_pemimpinGrup"));
                result.put("approverStaffITSecurity", resultSet.getString("c_staffItSecurity"));
            }

        } catch (SQLException e) {
            LogUtil.error(getClass().getName(), e, e.getMessage());
        }

        return result;
    }

    public String getRequesterUsername(String formPrimaryKey) {
        DataSource dataSource = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");

        String result = "";
        try (Connection con = dataSource.getConnection()){
            String sql = "SELECT c_nrik FROM app_fd_userid_requests WHERE id = ? LIMIT 1";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, formPrimaryKey);
            statement.executeQuery();

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result = resultSet.getString("c_nrik");
            }
        } catch (Exception e) {
            LogUtil.error(getClass().getName(), e, e.getMessage());
        }

        LogUtil.info(getClass().getName(), "result " + result);
        return result;
    }
}
