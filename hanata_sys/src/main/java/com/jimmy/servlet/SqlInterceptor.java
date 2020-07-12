package com.jimmy.servlet;

import com.jimmy.bean.User;
import com.jimmy.utils.SnowFlake;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jimmy He
 * @date 2020-06-10
 * 运行此方法NPE 待研究
 */


@Intercepts(
        {
                @Signature(
                        type = Executor.class,
                        method = "query",
                        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
                )
        }
)
public class SqlInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(SqlInterceptor.class);

    @Autowired
    SnowFlake mySnowFlask;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        logger.info("执行intercept方法:{}", invocation.toString());
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameterObject = args[1];

        // mapper.method's path ex:com.mapper.UserMapper
        String id = ms.getId();

        // sql cmd type
        String sqlCmdType = ms.getSqlCommandType().toString();
        System.out.println("==========="+sqlCmdType);
        if(sqlCmdType.equals(SqlCommandType.SELECT.toString())){
            return invocation.proceed();
        }


        BoundSql boundSql = ms.getBoundSql(parameterObject);
        String oriSql = boundSql.getSql();
        logger.info("original sql:{}", oriSql);

        // post process , handle with new Sql
        String newSql = "";
        if (sqlCmdType.equals(SqlCommandType.INSERT.toString())) {
            String regex = "\\(.*?\\)";
            List<String> matchers = getMatchers(oriSql, regex);
            String field = matchers.get(0);
            String values = matchers.get(1);

            String userId = RequestContext.get().getUser().getUid();
            Date datetime = Calendar.getInstance().getTime();
            String uid = String.valueOf(mySnowFlask.nextId());

            String newField = field.replaceFirst("\\)", "uid,createTime,createUserId,deleted)");
            String newValues = values.replaceFirst("\\)", "" + userId + "," + datetime + "," + uid + "," + "0");

            newSql = oriSql.replace(field, newField).replace(values, newValues);
        }

        // new query obj
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), newSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
        MappedStatement newMs = newMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }

        Object[] queryArgs = invocation.getArgs();
        queryArgs[0] = newMs;

        return invocation.proceed();


    }

    @Override
    public Object plugin(Object target) {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {

    }

    public static List<String> getMatchers(String regex, String source) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(source);
        ArrayList<String> list = new ArrayList<>();
        while (m.find()) {
            list.add(m.group());
        }
        return list;
    }

    private MappedStatement newMappedStatement (MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new
                MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(ms.getKeyProperties()[0]);
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    /**
     * 定义一个内部辅助类，作用是包装 SQL
     */
    class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;
        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }

    }
}
