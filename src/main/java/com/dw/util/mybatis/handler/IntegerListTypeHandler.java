package com.dw.util.mybatis.handler;

import com.dw.util.StringUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@MappedJdbcTypes(JdbcType.VARCHAR)
public class IntegerListTypeHandler extends BaseTypeHandler<List<Integer>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Integer> parameter, JdbcType jdbcType) throws SQLException {
        StringBuilder sb = new StringBuilder();
        for (Integer p : parameter){
            sb.append(String.valueOf(p));
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() -1);
        ps.setString(i, sb.toString());
    }

    @Override
    public List<Integer> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return StringUtil.splitStringToIntList(value, ",");
    }

    @Override
    public List<Integer> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return StringUtil.splitStringToIntList(value, ",");
    }

    @Override
    public List<Integer> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return StringUtil.splitStringToIntList(value, ",");
    }
}
