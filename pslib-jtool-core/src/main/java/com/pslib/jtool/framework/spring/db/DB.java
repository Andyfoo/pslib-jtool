package com.pslib.jtool.framework.spring.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.pslib.jtool.db.DbTool;
import com.pslib.jtool.db.sqlformatter.SqlFormatter;
import com.pslib.jtool.framework.hutool.json.HutoolJsonUtil;
import com.pslib.jtool.util.ArrayUtil;
import com.pslib.jtool.util.DateUtil;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

/**
 * 数据库操作类
 * 
 * @author FH
 *
 */
public class DB {
	protected static final Logger log = LoggerFactory.getLogger(DB.class);
	private boolean debug = false;

	protected JdbcTemplate jtpl;
	protected DataSourceTransactionManager transManager;
	protected DataSource dataSource;

	private String dbType;
	private String dbProductName;
	private String dbProductVersion;

	private boolean isMySQL;
	private boolean isOracle;
	private boolean isSQLite;

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * JDBC
	 */
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jtpl = jdbcTemplate;
	}

	/**
	 * 设置数据源
	 */
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		setDbInfo();
	}

	/**
	 * 事务管理
	 */
	@Autowired
	public void setTransManager(DataSourceTransactionManager transManager) {
		this.transManager = transManager;
	}

	@PreDestroy
	public void destroy() {
		try {
			dataSource.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取DataSource
	 * 
	 * @return
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * 获取JdbcTemplate
	 * 
	 * @return
	 */
	public JdbcTemplate getJtpl() {
		return jtpl;
	}

	/**
	 * 获取DataSourceTransactionManager
	 * 
	 * @return
	 */
	public DataSourceTransactionManager getTransManager() {
		return transManager;
	}

	/**
	 * 设置数据库信息
	 */
	public void setDbInfo() {
		try {
			setDbProductName(dataSource.getConnection().getMetaData().getDatabaseProductName());
			setDbProductVersion(dataSource.getConnection().getMetaData().getDatabaseProductVersion());
			setDbType(dbProductName.toLowerCase());
		} catch (SQLException e) {
			log.error("", e);
		}

	}

	/**
	 * 获取数据库类型
	 * 
	 * @return
	 */
	public String getDbType() {
		return dbType;
	}

	/**
	 * 设置数据库类型
	 * 
	 * @param dbType
	 */
	public void setDbType(String dbType) {
		this.dbType = dbType;
		isMySQL = getDbType().equals("mysql");
		isOracle = getDbType().equals("oracle");
		isSQLite = getDbType().equals("sqlite");
	}

	/**
	 * 判断数据库是否是mysql
	 * 
	 * @return
	 */
	public boolean isMySQL() {
		return isMySQL;
	}

	/**
	 * 判断数据库是否是isSQLite
	 */
	public boolean isSQLite() {
		return isSQLite;
	}

	/**
	 * 判断数据库是否是oracle
	 */
	public boolean isOracle() {
		return isOracle;
	}

	/**
	 * 设置数据库产品名称
	 * 
	 * @return
	 */
	public String getDbProductName() {
		return dbProductName;
	}

	/**
	 * 获取数据库产品名称
	 * 
	 * @param dbProductName
	 */
	public void setDbProductName(String dbProductName) {
		this.dbProductName = dbProductName;
	}

	/**
	 * 设置数据库版本
	 * 
	 * @return
	 */
	public String getDbProductVersion() {
		return dbProductVersion;
	}

	/**
	 * 获取数据库版本
	 * 
	 * @param dbProductVersion
	 */
	public void setDbProductVersion(String dbProductVersion) {
		this.dbProductVersion = dbProductVersion;
	}

	/**
	 * 更新表
	 * 
	 * @param tbName 表名
	 * @param map    字段及对应值
	 * @param where  更新条件
	 * @return
	 * @throws Exception
	 */
	public int q_update(String tbName, JSONObject map, String where) throws Exception {
		return q_update(tbName, map, where, new Object[] {});
	}

	/**
	 * 更新表
	 * 
	 * @param tbName      表名
	 * @param map         字段及对应值
	 * @param where       更新条件
	 * @param whereParams 更新条件值
	 * @return
	 * @throws Exception
	 */
	public int q_update(String tbName, JSONObject map, String where, JSONArray whereParams) throws Exception {
		return q_update(tbName, map, where, whereParams != null ? whereParams.toArray() : new Object[] {});
	}

	/**
	 * 更新表
	 * 
	 * @param tbName      表名
	 * @param map         字段及对应值
	 * @param where       更新条件
	 * @param whereParams 更新条件值
	 * @return
	 * @throws Exception
	 */
	public int q_update(String tbName, JSONObject map, String where, Object[] whereParams) throws Exception {
		JSONArray param = new JSONArray();

		StringBuffer sqlKeys = new StringBuffer();

		Set<Entry<String, Object>> entrys = map.entrySet();
		String key;
		Object val;
		for (Entry<String, Object> entry : entrys) {
			key = entry.getKey();
			val = entry.getValue();

			if (val instanceof StringBuffer) {
				sqlKeys.append(key + "=" + ((StringBuffer) val).toString() + ",");
			} else {
				sqlKeys.append(key + "=?,");
				param.add(val);
			}

		}
		sqlKeys.deleteCharAt(sqlKeys.length() - 1);

		StringBuffer sql = new StringBuffer();

		sql.append("update " + tbName + " set ");
		sql.append(sqlKeys.toString());
		if (where != null && where.length() > 0) {
			sql.append(" where ");
			sql.append(where);
		}
		if (whereParams != null && whereParams.length > 0) {
			for (Object whereParam : whereParams) {
				param.add(whereParam);
			}

		}

		// System.out.println(param);
		double t = DbTool.getTime();
		if (debug)
			log.info("exe sql start" + debugSql(sql.toString(), param));
		int r = jtpl.update(sql.toString(), param.toArray());
		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 插入表
	 * 
	 * @param tbName 表名
	 * @param map    字段及对应值
	 * @return 如果是MySQL则返回主键ID
	 * @throws Exception
	 */
	public int q_insert(String tbName, JSONObject map) throws Exception {
		return q_insert(tbName, map, true);
	}

	public int q_insert(String tbName, JSONObject map, boolean returnMysqlLastId) throws Exception {

		JSONArray param = new JSONArray();

		StringBuffer sqlKeys = new StringBuffer();
		StringBuffer sqlValues = new StringBuffer();

		Set<Entry<String, Object>> entrys = map.entrySet();
		String key;
		Object val;
		for (Entry<String, Object> entry : entrys) {
			key = entry.getKey();
			val = entry.getValue();
			if (val instanceof String && ((String) val).trim().toLowerCase().endsWith(".nextval")) {
				sqlKeys.append(key + ",");
				sqlValues.append(((String) val) + ",");
			} else if (val instanceof StringBuffer) {
				sqlKeys.append(key + ",");
				sqlValues.append(((StringBuffer) val).toString() + ",");
			} else {
				sqlKeys.append(key + ",");
				sqlValues.append("?,");

				param.add(val);
			}
		}
		sqlKeys.deleteCharAt(sqlKeys.length() - 1);
		sqlValues.deleteCharAt(sqlValues.length() - 1);

		StringBuffer sql = new StringBuffer();

		sql.append("insert into " + tbName + " (");
		sql.append(sqlKeys.toString());
		sql.append(") values (");
		sql.append(sqlValues.toString());
		sql.append(") ");
		// System.out.println(param);

		double t = DbTool.getTime();
		if (debug)
			log.info("exe sql start" + debugSql(sql.toString(), param));
		// int r = jtpl.update(sql.toString(), param.toArray());

		int r = 0;

		if (returnMysqlLastId && (isMySQL() || isSQLite())) {// mysql返回 自增长ID
			DataSourceTransactionManager tran = this.getTransManager();
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			TransactionStatus status = tran.getTransaction(def);
			try {
				jtpl.update(sql.toString(), param.toArray());

				r = jtpl.queryForObject("SELECT " + (isSQLite() ? "LAST_INSERT_ROWID" : "LAST_INSERT_ID") + "()", Integer.class);
			} catch (Exception ex) {
				tran.rollback(status);
				log.error("DB error", ex);
			} finally {
				tran.commit(status);
			}
		} else {
			r = jtpl.update(sql.toString(), param.toArray());
		}

		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 插入表
	 * 
	 * @param tbName 表名
	 * @param map    字段及对应值
	 * @return 如果是MySQL则返回主键ID
	 * @throws Exception
	 */
	public long q_insert_MysqlLastId(String tbName, JSONObject map) throws Exception {

		JSONArray param = new JSONArray();

		StringBuffer sqlKeys = new StringBuffer();
		StringBuffer sqlValues = new StringBuffer();

		Set<Entry<String, Object>> entrys = map.entrySet();
		String key;
		Object val;
		for (Entry<String, Object> entry : entrys) {
			key = entry.getKey();
			val = entry.getValue();
			if (val instanceof StringBuffer) {
				sqlKeys.append(key + ",");
				sqlValues.append(((StringBuffer) val).toString() + ",");
			} else {
				sqlKeys.append(key + ",");
				sqlValues.append("?,");

				param.add(val);
			}
		}
		sqlKeys.deleteCharAt(sqlKeys.length() - 1);
		sqlValues.deleteCharAt(sqlValues.length() - 1);

		StringBuffer sql = new StringBuffer();

		sql.append("insert into " + tbName + " (");
		sql.append(sqlKeys.toString());
		sql.append(") values (");
		sql.append(sqlValues.toString());
		sql.append(") ");
		// System.out.println(param);

		double t = DbTool.getTime();
		if (debug)
			log.info("exe sql start" + debugSql(sql.toString(), param));
		// int r = jtpl.update(sql.toString(), param.toArray());

		long r = 0;

		DataSourceTransactionManager tran = this.getTransManager();
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = tran.getTransaction(def);
		try {
			jtpl.update(sql.toString(), param.toArray());

			r = jtpl.queryForObject("SELECT " + (isSQLite() ? "LAST_INSERT_ROWID" : "LAST_INSERT_ID") + "()", Long.class);
		} catch (Exception ex) {
			tran.rollback(status);
			log.error("DB error", ex);
		} finally {
			tran.commit(status);
		}

		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 替换更新记录，MySQL专用
	 * 
	 * @param tbName 表名
	 * @param map    字段及对应值
	 * @return 如果是MySQL则返回主键ID
	 * @throws Exception
	 */
	public int q_replace(String tbName, JSONObject map) throws Exception {

		JSONArray param = new JSONArray();

		StringBuffer sqlKeys = new StringBuffer();
		StringBuffer sqlValues = new StringBuffer();

		Set<Entry<String, Object>> entrys = map.entrySet();
		String key;
		Object val;
		for (Entry<String, Object> entry : entrys) {
			key = entry.getKey();
			val = entry.getValue();
			if (val instanceof StringBuffer) {
				sqlKeys.append(key + ",");
				sqlValues.append(((StringBuffer) val).toString() + ",");
			} else {
				sqlKeys.append(key + ",");
				sqlValues.append("?,");

				param.add(val);
			}
		}
		sqlKeys.deleteCharAt(sqlKeys.length() - 1);
		sqlValues.deleteCharAt(sqlValues.length() - 1);

		StringBuffer sql = new StringBuffer();

		sql.append("replace into " + tbName + " (");
		sql.append(sqlKeys.toString());
		sql.append(") values (");
		sql.append(sqlValues.toString());
		sql.append(") ");
		// System.out.println(param);

		double t = DbTool.getTime();
		if (debug)
			log.info("exe sql start" + debugSql(sql.toString(), param));
		// int r = jtpl.update(sql.toString(), param.toArray());

		int r = 0;

		if (isMySQL() || isSQLite()) {// mysql返回 自增长ID
			r = jtpl.update(sql.toString(), param.toArray());
		} else {
			log.error("(replace into table ...) only mysql");
		}

		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 删除记录
	 * 
	 * @param tbName 表名
	 * @param where  更新条件
	 * @return
	 * @throws Exception
	 */
	public int q_del(String tbName, String where) throws Exception {
		return q_del(tbName, where, new Object[] {});
	}

	/**
	 * 删除记录
	 * 
	 * @param tbName      表名
	 * @param where       更新条件
	 * @param whereParams 更新条件值
	 * @return
	 * @throws Exception
	 */
	public int q_del(String tbName, String where, JSONArray whereParams) throws Exception {
		return q_del(tbName, where, whereParams != null ? whereParams.toArray() : new Object[] {});
	}

	/**
	 * 删除记录
	 * 
	 * @param tbName      表名
	 * @param where       更新条件
	 * @param whereParams 更新条件值
	 * @return
	 * @throws Exception
	 */
	public int q_del(String tbName, String where, Object[] whereParams) throws Exception {
		JSONArray param = new JSONArray();

		StringBuffer sql = new StringBuffer();

		sql.append("delete from " + tbName + " where ");
		sql.append(where);

		if (whereParams != null && whereParams.length > 0) {
			for (Object whereParam : whereParams) {
				param.add(whereParam);
			}

		}

		// System.out.println(param);
		double t = DbTool.getTime();
		if (debug)
			log.info("exe sql start" + debugSql(sql.toString(), param));
		int r = jtpl.update(sql.toString(), param.toArray());
		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 查询单记录
	 * 
	 * @param sql
	 * @return
	 */
	public JSONObject q_get_map(String tableName, String where, String field, String orderby, JSONArray param) throws Exception {
		return q_get_map(tableName, where, field, orderby, param == null ? null : param.toArray());
	}

	/**
	 * 查询单记录
	 * 
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public JSONObject q_get_map(String tableName, String where, String field, String orderby, Object[] param) throws Exception {
		String sql = "select ";
		if (field != null && field.length() > 0) {
			sql += field;
		} else {
			sql += "*";
		}
		sql += " from " + tableName;
		if (where != null && where.length() > 0) {
			sql += " where " + where;
		}
		if (orderby != null && orderby.length() > 0) {
			sql += " order by " + orderby;
		}

		if (isMySQL() || isSQLite()) {
			sql = sql + " limit 1";
		} else {
			sql = "select * from (" + sql + ") where rownum <=1 ";
		}

		double t = DbTool.getTime();
		if (debug)
			log.info("exe sql start" + debugSql(sql, param));

		JSONArray r = null;
		try {
			r = param == null ? DbTool.field2json(jtpl.queryForList(sql)) : DbTool.field2json(jtpl.queryForList(sql, param));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
		return r != null && r.size() > 0 ? r.getJSONObject(0) : null;
	}

	/**
	 * 列表，限制数量--带参数
	 * 
	 * @param sql
	 * @param num
	 * @return
	 */
	public JSONArray q_get_list(String tableName, String where, String field, String orderby, JSONArray param, int num) throws Exception {
		return q_get_list(tableName, where, field, orderby, param == null ? null : param.toArray(), num);
	}

	/**
	 * 列表，限制数量--带参数
	 * 
	 * @param sql
	 * @param param
	 * @param num
	 * @return
	 * @throws Exception
	 */
	public JSONArray q_get_list(String tableName, String where, String field, String orderby, Object[] param, int num) throws Exception {
		String sql = "select ";
		if (field != null && field.length() > 0) {
			sql += field;
		} else {
			sql += "*";
		}
		sql += " from " + tableName;
		if (where != null && where.length() > 0) {
			sql += " where " + where;
		}
		if (orderby != null && orderby.length() > 0) {
			sql += " order by " + orderby;
		}

		if (isMySQL() || isSQLite()) {
			sql = sql + " limit 1";
		} else {
			sql = "select * from (" + sql + ") where rownum <=1 ";
		}
		// System.out.println("sql:" + sql);
		if (isMySQL() || isSQLite()) {
			sql = sql + " limit ? ";
		} else {
			sql = "select * from (" + sql + ") where rownum <= ? ";
		}
		param = ArrayUtil.append(param, num);
		// System.out.println(sql);
		double t = DbTool.getTime();
		if (debug)
			log.info("exe sql start" + debugSql(sql, param));
		JSONArray r = DbTool.field2json(jtpl.queryForList(sql, param));
		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 批量更新或插入
	 * 
	 * @param sql 多个sql
	 * @return
	 * @throws Exception
	 */
	public int[] batchUpdate(String sql[]) throws Exception {
		double t = DbTool.getTime();
		if (debug)
			log.info("exe sql start");
		int[] r = jtpl.batchUpdate(sql);
		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 批量更新或插入--带参数
	 * 
	 * @param sql
	 * @param batchArgs 多个参数
	 * @return
	 * @throws Exception
	 */
	public int[] batchUpdate(String sql, List<Object[]> batchArgs) throws Exception {

		double t = DbTool.getTime();
		if (debug)
			log.info("exe sql start" + debugSql(sql, batchArgs));
		int[] r = jtpl.batchUpdate(sql, batchArgs);

		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 更新或插入
	 * 
	 * @param sql
	 * @return
	 */
	public int update(String sql) throws Exception {
		double t = DbTool.getTime();
		if (debug)
			log.info("exe sql start" + debugSql(sql));
		int r = jtpl.update(sql);
		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 更新或插入--带参数
	 * 
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int update(String sql, JSONArray param) throws Exception {
		return update(sql, param.toArray());
	}

	/**
	 * 更新或插入--带参数
	 * 
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int update(String sql, Object[] param) throws Exception {

		double t = DbTool.getTime();
		if (debug)
			log.info("exe sql start" + debugSql(sql, param));
		int r = jtpl.update(sql, param);
		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 查询单条记录
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public JSONObject queryMap(String sql) throws Exception {
		double t = DbTool.getTime();

		if (debug)
			log.info("exe sql start" + debugSql(sql));

		if (isMySQL() || isSQLite()) {
			sql = sql + " limit 1";
		} else {
			sql = "select * from (" + sql + ") where rownum <=1 ";
		}

		JSONArray r = null;
		try {
			r = DbTool.field2json(jtpl.queryForList(sql));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
		return r != null && r.size() > 0 ? r.getJSONObject(0) : null;
	}

	/**
	 * 查询单记录--带参数
	 * 
	 * @param sql
	 * @return
	 */
	public JSONObject queryMap(String sql, JSONArray param) throws Exception {
		return queryMap(sql, param.toArray());
	}

	/**
	 * 查询单记录--带参数
	 * 
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public JSONObject queryMap(String sql, Object[] param) throws Exception {
		double t = DbTool.getTime();
		if (debug)
			log.info("exe sql start" + debugSql(sql, param));
		if (isMySQL() || isSQLite()) {
			sql = sql + " limit 1";
		} else {
			sql = "select * from (" + sql + ") where rownum <=1 ";
		}

		JSONArray r = null;
		try {
			r = DbTool.field2json(jtpl.queryForList(sql, param));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
		return r != null && r.size() > 0 ? r.getJSONObject(0) : null;
	}

	/**
	 * 查询列表
	 * 
	 * @param sql
	 * @return
	 */
	public JSONArray queryList(String sql) throws Exception {
		double t = DbTool.getTime();
		if (debug)
			log.info("exe sql start" + debugSql(sql));
		JSONArray r = DbTool.field2json(jtpl.queryForList(sql));
		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 查询列表--带参数
	 * 
	 * @param sql
	 * @return
	 */
	public JSONArray queryList(String sql, JSONArray param) throws Exception {
		return queryList(sql, param.toArray());
	}

	/**
	 * 查询列表--带参数
	 * 
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public JSONArray queryList(String sql, Object[] param) throws Exception {
		double t = DbTool.getTime();
		if (debug)
			log.info("exe sql start" + debugSql(sql, param));
		JSONArray r = DbTool.field2json(jtpl.queryForList(sql, param));
		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 列表，限制数量
	 * 
	 * @param sql
	 * @param num
	 * @return
	 */
	public JSONArray queryList(String sql, int num) throws Exception {
		// System.out.println("sql:" + sql);
		if (isMySQL() || isSQLite()) {
			sql = sql + " limit ? ";
		} else {
			sql = "select * from (" + sql + ") where rownum <= ? ";
		}
		double t = DbTool.getTime();
		if (debug)
			log.info("exe sql start" + debugSql(sql, new Object[] { num }));

		JSONArray r = DbTool.field2json(jtpl.queryForList(sql, num));
		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 列表，限制数量--带参数
	 * 
	 * @param sql
	 * @param num
	 * @return
	 */
	public JSONArray queryList(String sql, JSONArray param, int num) throws Exception {
		return queryList(sql, param.toArray(), num);
	}

	/**
	 * 列表，限制数量--带参数
	 * 
	 * @param sql
	 * @param param
	 * @param num
	 * @return
	 * @throws Exception
	 */
	public JSONArray queryList(String sql, Object[] param, int num) throws Exception {
		if (isMySQL() || isSQLite()) {
			sql = sql + " limit ? ";
		} else {
			sql = "select * from (" + sql + ") where rownum <= ? ";
		}
		param = ArrayUtil.append(param, num);
		double t = DbTool.getTime();
		if (debug)
			log.info("exe sql start" + debugSql(sql, param));

		JSONArray r = DbTool.field2json(jtpl.queryForList(sql, param));
		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 分页列表
	 * 
	 * @param sql
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public JSONArray queryList(String sql, int pageSize, int curPage) throws Exception {
		// System.out.println("sql:" + sql);
		int beginNum = (curPage - 1) * pageSize;
		int endNum = (curPage * pageSize);
		/*
		 * String ptn = "SELECT * FROM (" + "SELECT T.*, ROWNUM RN, (SELECT COUNT(*) FROM (%%s)) as TOTAL_RECORD FROM (%%s) T WHERE ROWNUM <= %d) WHERE RN > %d" ;
		 */
		Object[] param = new Object[2];
		if (isMySQL() || isSQLite()) {
			sql = sql + " limit ?, ?";
			param[0] = beginNum;
			param[1] = pageSize;
		} else {
			sql = "SELECT * FROM (" + "SELECT T.*, ROWNUM RN FROM (" + sql + ") T WHERE ROWNUM <= ?) WHERE RN > ?";
			param[0] = endNum;
			param[1] = beginNum;
		}

		double t = DbTool.getTime();
		if (debug)
			log.info("exe sql start" + debugSql(sql, param));
		JSONArray r = DbTool.field2json(jtpl.queryForList(sql, param));
		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 分页列表--带参数
	 * 
	 * @param sql
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public JSONArray queryList(String sql, JSONArray param, int pageSize, int curPage) throws Exception {
		return queryList(sql, param.toArray(), pageSize, curPage);
	}

	/**
	 * 分页列表--带参数
	 * 
	 * @param sql
	 * @param param
	 * @param pageSize
	 * @param curPage
	 * @return
	 * @throws Exception
	 */
	public JSONArray queryList(String sql, Object[] param, int pageSize, int curPage) throws Exception {
		// System.out.println("sql:" + sql);
		int beginNum = (curPage - 1) * pageSize;
		int endNum = (curPage * pageSize);

		if (isMySQL() || isSQLite()) {
			sql = sql + " limit ?, ?";
			param = ArrayUtil.append(param, beginNum);
			param = ArrayUtil.append(param, pageSize);
		} else {
			sql = "SELECT * FROM (" + "SELECT T.*, ROWNUM RN FROM (" + sql + ") T WHERE ROWNUM <= ?) WHERE RN > ?";
			param = ArrayUtil.append(param, endNum);
			param = ArrayUtil.append(param, beginNum);
		}

		double t = DbTool.getTime();
		if (debug)
			log.info("exe sql start" + debugSql(sql, param));

		JSONArray r = DbTool.field2json(jtpl.queryForList(sql, param));
		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 分页列表--带参数
	 * 
	 * @param sql
	 * @param param
	 * @param pageSize
	 * @param curPage
	 * @return
	 * @throws Exception
	 */
	public JSONObject queryPageList(String sql, JSONArray param, int pageSize, int curPage) throws Exception {
		return queryPageList(sql, param.toArray(), pageSize, curPage);
	}

	public JSONObject queryPageList(String sql, Object[] param, int pageSize, int curPage) throws Exception {
		int totalNum = count(sql, param);
		int pageNum = totalNum / pageSize + (totalNum % pageSize == 0 ? 0 : 1);
		if (curPage <= 0 || curPage > pageNum)
			curPage = pageNum;
		int beginNum = (curPage - 1) * pageSize;
		int endNum = (curPage * pageSize);
		JSONArray r = new JSONArray();
		if (totalNum > 0) {
			if (isMySQL() || isSQLite()) {
				sql = sql + " limit ?, ?";
				param = ArrayUtil.append(param, beginNum);
				param = ArrayUtil.append(param, pageSize);
			} else {
				sql = "SELECT * FROM (" + "SELECT T.*, ROWNUM RN FROM (" + sql + ") T WHERE ROWNUM <= ?) WHERE RN > ?";
				param = ArrayUtil.append(param, endNum);
				param = ArrayUtil.append(param, beginNum);
			}

			double t = DbTool.getTime();
			if (debug)
				log.info("exe sql start" + debugSql(sql, param));
			r = DbTool.field2json(jtpl.queryForList(sql, param));
			if (debug)
				log.info("exe sql finish:" + DbTool.runTime(t));
		}

		JSONObject json = new JSONObject();
		json.set("list", r);
		json.set("totalNum", totalNum);
		json.set("pageNum", pageNum);
		json.set("curPage", curPage);
		json.set("pageSize", pageSize);
		return json;
	}

	/**
	 * 统计数量
	 * 
	 * @param sql
	 * @return
	 */
	public int count(String sql) throws Exception {
		double t = DbTool.getTime();
		sql = replaceOrderBy(sql);

		if (isMySQL() || isSQLite()) {
			if (isContainSubQuery(sql)) {
				sql = addMysqlCountStr(sql);
				if (debug)
					log.info("exe sql start" + debugSql(sql));

				DataSourceTransactionManager tran = getTransManager();
				DefaultTransactionDefinition def = new DefaultTransactionDefinition();
				TransactionStatus status = tran.getTransaction(def);
				try {
					jtpl.queryForList(sql);

					sql = "SELECT FOUND_ROWS()";
					int r = jtpl.queryForObject(sql, Integer.class);
					if (debug)
						log.info("exe sql finish:" + DbTool.runTime(t));
					return r;
				} catch (Exception ex) {
					tran.rollback(status);
					log.error("DB error", ex);
				} finally {
					tran.commit(status);
				}
				if (debug)
					log.info("exe sql finish:" + DbTool.runTime(t));
				return 0;
			} else {
				sql = addCountStr(sql);
			}
		} else {
			if (isContainSubQuery(sql)) {
				sql = "select count(*) from (" + sql + ") ";
			} else {
				sql = addCountStr(sql);
			}
		}
		if (debug)
			log.info("exe sql start" + debugSql(sql));

		int r = jtpl.queryForObject(sql, Integer.class);
		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 统计数量--带参数
	 * 
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int count(String sql, JSONArray param) throws Exception {
		return count(sql, param.toArray());
	}

	/**
	 * 统计数量--带参数
	 * 
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int count(String sql, Object[] param) throws Exception {
		double t = DbTool.getTime();
		sql = replaceOrderBy(sql);

		if (isMySQL() || isSQLite()) {
			if (isContainSubQuery(sql)) {
				sql = addMysqlCountStr(sql);
				if (debug)
					log.info("exe sql start" + debugSql(sql, param));

				DataSourceTransactionManager tran = getTransManager();
				DefaultTransactionDefinition def = new DefaultTransactionDefinition();
				TransactionStatus status = tran.getTransaction(def);
				try {
					jtpl.queryForList(sql, param);

					sql = "SELECT FOUND_ROWS()";
					int r = jtpl.queryForObject(sql, Integer.class);
					if (debug)
						log.info("exe sql finish:" + DbTool.runTime(t));
					return r;
				} catch (Exception ex) {
					tran.rollback(status);
					log.error("DB error", ex);
				} finally {
					tran.commit(status);
				}
				if (debug)
					log.info("exe sql finish:" + DbTool.runTime(t));
				return 0;
			} else {
				sql = addCountStr(sql);
			}
		} else {
			if (isContainSubQuery(sql)) {
				sql = "select count(*) from (" + sql + ") ";
			} else {
				sql = addCountStr(sql);
			}
		}
		if (debug)
			log.info("exe sql start" + debugSql(sql, param));
			int r = jtpl.queryForObject(sql, new RowMapper<Integer>() {
				public Integer mapRow(java.sql.ResultSet res, int index) throws SQLException {
					return res.getInt(0);
				}
			}, param);
		// System.out.println(r);
		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 获取数量
	 * 
	 * @param sql
	 * @return
	 */
	public int queryInt(String sql) throws Exception {
		double t = DbTool.getTime();
		if (debug)
			log.info("exe sql start" + debugSql(sql));
		int r = jtpl.queryForObject(sql, Integer.class);
		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 获取数量--带参数
	 * 
	 * @param sql
	 * @return
	 */
	public int queryInt(String sql, JSONArray param) throws Exception {
		return queryInt(sql, param.toArray());
	}

	/**
	 * 获取数量--带参数
	 * 
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int queryInt(String sql, Object[] param) throws Exception {

		double t = DbTool.getTime();
		if (debug)
			log.info("exe sql start" + debugSql(sql, param));
		int r = jtpl.queryForObject(sql, new RowMapper<Integer>() {
			public Integer mapRow(java.sql.ResultSet res, int index) throws SQLException {
				return res.getInt(0);
			}
		}, param);
		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 获取数量
	 * 
	 * @param sql
	 * @return
	 */
	public long queryLong(String sql) throws Exception {
		double t = DbTool.getTime();
		if (debug)
			log.info("exe sql start" + debugSql(sql));
		long r = jtpl.queryForObject(sql, Long.class);
		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 获取数量--带参数
	 * 
	 * @param sql
	 * @return
	 */
	public long queryLong(String sql, JSONArray param) throws Exception {
		return queryLong(sql, param.toArray());
	}

	/**
	 * 获取数量--带参数
	 * 
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public long queryLong(String sql, Object[] param) throws Exception {

		double t = DbTool.getTime();
		if (debug)
			log.info("exe sql start" + debugSql(sql, param));

		long r = jtpl.queryForObject(sql, new RowMapper<Long>() {
			public Long mapRow(java.sql.ResultSet res, int index) throws SQLException {
				return res.getLong(0);
			}
		}, param);
		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 执行sql
	 * 
	 * @param sql
	 * @return
	 */
	public void query(String sql) throws Exception {
		double t = DbTool.getTime();
		if (debug)
			log.info("exe sql start" + debugSql(sql));
		jtpl.execute(sql);
		if (debug)
			log.info("exe sql finish:" + DbTool.runTime(t));
	}

	/**
	 * 获取下一个SEQUENCE sql
	 * 
	 * @param sName
	 * @return
	 */
	public String getSequenceNext(String sName) {
		return sName + ".nextval";
	}

	/**
	 * 获取下一个SEQUENCE val
	 * 
	 * @param sName
	 * @return
	 * @throws Exception
	 */
	public long getSequenceNextval(String sName) throws Exception {
		return queryLong("SELECT " + sName + ".nextval FROM dual");
	}

	/**
	 * 获取当前SEQUENCE val
	 * 
	 * @param sName
	 * @return
	 * @throws Exception
	 */
	public long getSequenceCurrval(String sName) throws Exception {
		return queryLong("SELECT " + sName + ".currval FROM dual");
	}

	/**
	 * 美化sql
	 * 
	 * @param sql
	 * @return
	 */
	public String formatSql(String sql) {
		return SqlFormatter.format(sql, "\t");
	}

	public String formatSql(String sql, List<Object> param) {
		List<Object> param2 = new ArrayList<Object>();
		int len = param.size();
		Object item;
		for (int i = 0; i < len; i++) {
			item = param.get(i);
			if (item instanceof java.util.Date) {
				param2.add(DateUtil.getDate((Date) item, "yyyy-MM-dd HH:mm:ss"));
			} else {
				param2.add(String.valueOf(item));
			}

		}
		return SqlFormatter.format(sql, "\t", param2);
	}

	public String formatSql(String sql, Object[] _param) {
		return formatSql(sql, Arrays.asList(_param));
	}

	/**
	 * 替换order by 为空
	 * 
	 * @param sql
	 * @return
	 */
	public String replaceOrderBy(String sql) {
		// sql = sql.replaceAll("(?i)order\\s+by\\s+[^\\s]+\\s*$", " ");
		sql = sql.replaceAll("(?i)order\\s+by\\s+.+$", " ");
		// sql = sql.replaceAll("(?i)order\\s+by\\s+[^\\s]+\\s*(LIMIT\\s*.*)", " $1 ");
		return sql;
	}

	/**
	 * 增加count()
	 * 
	 * @param sql
	 * @return
	 */
	public String addCountStr(String sql) {
		String tmpSql = sql + "";
		// sql = sql.replaceAll("(?i)\\(\\s*select.*?from.*?\\)", "1");
		sql = sql.replaceAll("(?i)^\\s*(select\\s+).*?(\\s+from\\s+.*)", " $1 count(1) $2");
		if (tmpSql.equals(sql)) {
			sql = "select count(*) from (" + sql + ") CountSQL ";
		}
		return sql;
	}

	/**
	 * 增加count()
	 * 
	 * @param sql
	 * @return
	 */
	public String addMysqlCountStr(String sql) {
		sql = sql.replaceAll("(?i)^\\s*(select\\s+)", " $1 SQL_CALC_FOUND_ROWS ");
		return sql + " limit 1";
	}

	/**
	 * 判断是否存在子查询
	 */
	public boolean isContainSubQuery(String sql) {
		Pattern p = Pattern.compile("\\s*\\(\\s*select\\s+", Pattern.CASE_INSENSITIVE);
		return p.matcher(sql).find();
	}

	/**
	 * 返回debug sql字符串
	 * 
	 * @param sql
	 * @return
	 */
	public String debugSql(String sql) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n---[sql]<<\n");
		sb.append(formatSql(sql));
		sb.append("\n---[sql]>>\n");
		return sb.toString();
	}

	public String debugSql(String sql, Object param) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n---[sql]<<\n");
		sb.append(formatSql(sql));
		if (param != null) {
			sb.append("\n>>>param:\n");
			sb.append(HutoolJsonUtil.toJsonPrettyStr(param));
		}
		sb.append("\n---[sql]>>\n");

		return sb.toString();
	}

	public String debugSql(String sql, Object[] param) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n---[sql]<<\n");
		sb.append(formatSql(sql, param));
		sb.append("\n---[sql]>>\n");
		return sb.toString();
	}

	public String debugSql(String sql, List<Object> param) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n---[sql]<<\n");
		sb.append(formatSql(sql, param));
		sb.append("\n---[sql]>>\n");
		return sb.toString();
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		DB t1 = new DB();
		t1.debug = true;
		JSONObject map = new JSONObject();
		map.set("sms_mobile", "sadfsa");
		map.set("sms_vcode", "1111");
		map.set("sms_create_time", new Date());
		t1.q_insert("sms_vcode", map);

		List<Object> param = new java.util.ArrayList<Object>();
		param.add(333);
		param.add("test");
		Object[] param2 = new Object[] { new Date() };

		// System.out.println(t1.formatSql("select * from aa order by ccd,bbs"));
		// System.out.println(t1.formatSql("select * from aa where tt=? and bb=? order by ccd,bbs", param));
		System.out.println(t1.debugSql("select * from aa where tt=? order by ccd,bbs", param2));
//		System.out.println(t1.formatSql(
//				"SELECT t1.*,t2.cat_name,(select aa from aaa),(select bb from bbb) FROM wiki_doc t1 left join wiki_catalog t2 on t1.doc_cid=t2.cat_id WHERE 1=1  and FIND_IN_SET(t1.doc_cid, (select cat_child from wiki_catalog where cat_id=?) )>0 ORDER BY doc_id DESC",
//				param2));

		// System.out.println(t1.addCountStr("SELECT t1.*,t2.cat_name FROM wiki_doc t1 left join wiki_catalog t2 on t1.doc_cid=t2.cat_id WHERE 1=1 and FIND_IN_SET(t1.doc_cid, (select cat_child
		// from wiki_catalog where cat_id=?) )>0 ORDER BY doc_id DESC"));
		// System.out.println(t1.addCountStr("SELECT t1.*,t2.cat_name,(select aa from aaa),(select bb from bbb) FROM wiki_doc t1 left join wiki_catalog t2 on t1.doc_cid=t2.cat_id WHERE 1=1 and
		// FIND_IN_SET(t1.doc_cid, (select cat_child from wiki_catalog where cat_id=?) )>0 ORDER BY doc_id DESC"));

//		
//		System.out.println(t1.addCountStr("\r\n\t sElect 2select , *,aa from aa order by ccd,bbs"));
//		System.out.println(t1.addCountStr("select a.*, (select * from bb) from aa,(select * from cc) order by ccd,bbs"));
//		
//		System.out.println(t1.addMysqlCountStr(" select *,aa from aa order by ccd,bbs"));
//		System.out.println(t1.addMysqlCountStr("select a.*, (select * from bb) from aa,(select * from cc) order by ccd,bbs"));
//		
//		System.out.println(t1.isContainSubQuery("\r\n\t select 2select , *,aa from aa order by ccd,bbs"));
//		System.out.println(t1.isContainSubQuery("select a.*, (SELECT * from bb) from aa,(SELECT * from cc) order by ccd,bbs"));
//		
		// System.out.println(t1.replaceOrderBy("select * from aa order by ccd,bbs"));
		// System.out.println(t1.replaceOrderBy("select * from aa order by ccd desc"));
		// System.out.println(t1.replaceOrderBy("select * from aa order by ccd desc,bbs asc"));
//		System.out.println(t1.replaceOrderBy("select * from aa order by ccd,bbs LIMIT 1"));
//		System.out.println(t1.replaceOrderBy("select * from aa order by ccd,bbs FOR UPDATE"));
//		System.out.println(t1.replaceOrderBy("select * from aa order by ccd,bbs PROCEDURE"));
//		

		// dataSource.getConnection().getMetaData().getDbProductName()
	}

}