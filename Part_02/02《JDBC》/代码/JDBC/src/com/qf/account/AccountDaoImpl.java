package com.qf.account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AccountDaoImpl implements AccountDao {
    public int insert(Account account){return 0;}
    public int delete(String cardNo){return 0;}
    //修改
    public int update(Account account){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String sql = "update account set password=?,name=?,balance=? where cardNo = ?";
        try {
            connection = DBUtils.getConnection();
            System.out.println("update:"+connection);
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,account.getPassword());
            preparedStatement.setString(2,account.getName());
            preparedStatement.setDouble(3,account.getBalance());
            preparedStatement.setString(4,account.getCardNo());
            int result = preparedStatement.executeUpdate();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.closeAll(null,preparedStatement,null);
        }
        return 0;
    }

    //查询单个
    public Account select(String cardNo){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Account account = null;

        String sql = "select * from account where cardNo = ?";
        try {
            connection = DBUtils.getConnection();
            System.out.println("select:"+connection);
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,cardNo);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                String cardNos = resultSet.getString("cardNo");
                String password = resultSet.getString("password");
                String name = resultSet.getString("name");
                double balance =resultSet.getDouble("balance");
                account = new Account(cardNos,password,name,balance);
            }
            return account;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.closeAll(null,preparedStatement,resultSet);
        }
        return null;
    }

    @Override
    public List<Account> selectAll() {
        return null;
    }
}
