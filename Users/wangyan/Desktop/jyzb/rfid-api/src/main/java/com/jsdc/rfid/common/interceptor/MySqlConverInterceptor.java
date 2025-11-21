package com.jsdc.rfid.common.interceptor;

import cn.hutool.core.util.ReflectUtil;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import java.sql.Connection;
import java.util.Properties;

@Intercepts({
        @Signature(
                type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class, Integer.class}
        )
})
public class MySqlConverInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        ReflectUtil.setFieldValue(statementHandler.getBoundSql(),"sql",sqlConvert(boundSql.getSql()));
        return invocation.proceed();
    }

    private String sqlConvert(String originalSql) {
        String neSql = originalSql.replaceAll("TO_CHAR","to_char");
        neSql = neSql.replaceAll("to_char","DATE_FORMAT");
        neSql = neSql.replaceAll("yyyy-MM-dd HH24:mi:ss","%Y-%m-%d %H:%i:%s");
        neSql = neSql.replaceAll("yyyy-MM-dd","%Y-%m-%d");
        neSql = neSql.replaceAll("yyyy-MM","%Y-%m");
        neSql = neSql.replaceAll("YYYY-MM","%Y-%m");
        neSql = neSql.replaceAll("yyyy","%Y");
        neSql = neSql.replaceAll("YYYY","%Y");
        return neSql;
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
