package com.pslib.jtool.framework.jfinal.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbPro;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.pslib.jtool.db.DbTool;
import com.pslib.jtool.util.ArrayUtil;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

public class DbExt {
	protected static final Logger logger = LoggerFactory.getLogger(DbExt.class);
	protected boolean debug = false;
	public DbPro db;
	private String dbType;
	private String dbProductName;
	private String dbProductVersion;

	private boolean isMySQL;
	private boolean isOracle;
	private boolean isSQLite;

	public DbExt(String configName) {
		db = Db.use(configName);
		setDbInfo();
	}

	public DbExt() {
		db = Db.use();
		setDbInfo();
	}

	public static DbExt use(String configName) {
		return new DbExt(configName);
	}

	public static DbExt use() {
		return new DbExt();
	}
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * 设置数据库信息
	 */
	public void setDbInfo() {
		try {
			setDbProductName(db.getConfig().getConnection().getMetaData().getDatabaseProductName());
			setDbProductVersion(db.getConfig().getConnection().getMetaData().getDatabaseProductVersion());
			setDbType(dbProductName.toLowerCase());
		} catch (SQLException e) {
			logger.error("", e);
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
		sql = sql.replaceAll("(?i)^\\s*(select\\s+).*(\\s+from\\s+.*)", " $1 count(1) $2");
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
	static Pattern p_isContainSubQuery = Pattern.compile("\\s*\\(\\s*select\\s+", Pattern.CASE_INSENSITIVE);

	public boolean isContainSubQuery(String sql) {
		return p_isContainSubQuery.matcher(sql).find();
	}

	/**
	 * 将列表转换为json(字段名小写)
	 * 
	 * @param lists
	 * @return
	 */
	public static JSONArray field2json(List<Record> lists) {
		JSONArray json = new JSONArray();
		for (Record rec : lists) {
			json.add(field2json(rec));
		}
		return json;
	}

	/**
	 * 将map转换为json(字段名小写)
	 * 
	 * @param map
	 * @return
	 */
	public static JSONObject field2json(Record rec) {
		if (rec == null)
			return null;
		JSONObject json = new JSONObject(true);
		String[] names = rec.getColumnNames();
		for (int i = 0; i < names.length; i++) {
			json.set(names[i].toLowerCase(), rec.get(names[i]));
		}
		return json;
	}

	/**
	 * UPDATE, INSERT or DELETE
	 * 
	 * <pre>
	 * update("insert into blog set title='11', content='22'");
	 * update("insert into blog set title=?, content=?", "aa", "bb");
	 * update("insert into blog set title=?, content=?", new Object[] { "cc", "dd" });
	 * </pre>
	 * 
	 * @param sql
	 * @param objects
	 * @return
	 */
	public int update(String sql, Object... paras) {
		double t = DbTool.getTime();
		if (debug)
			logger.info("exe sql start" + DbTool.debugSql(sql));
		int r = db.update(sql, paras);
		if (debug)
			logger.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * UPDATE, INSERT or DELETE
	 * 
	 * <pre>
	 * JSONArray arr = new JSONArray();
	 * arr.add("ee");
	 * arr.add("ff");
	 * update("insert into blog set title=?, content=?", arr);
	 * </pre>
	 * 
	 * @param sql
	 * @param param
	 * @return
	 * 
	 */
	public int update(String sql, JSONArray param) {
		return update(sql, param.toArray());
	}

	/**
	 * 批量提交
	 * 
	 * @param sql
	 * @param objects
	 * @return
	 */
	public int[] batchUpdate(String sql, Object[][] objects) {
		double t = DbTool.getTime();
		if (debug)
			logger.info("exe sql start" + DbTool.debugSql(sql));

		int[] r = db.batch(sql, objects, objects.length);

		if (debug)
			logger.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 批量提交
	 * 
	 * @param sql
	 * @param batchArgs
	 * @return
	 */
	public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
		Object[][] objects = new Object[batchArgs.size()][];
		for (int i = 0; i < batchArgs.size(); i++) {
			objects[i] = batchArgs.get(i);
		}
		return batchUpdate(sql, objects);

	}

	/**
	 * 批量提交
	 * 
	 * @param sqlList
	 * @return
	 */
	public int[] batchUpdate(List<String> sqlList) {
		double t = DbTool.getTime();
		if (debug)
			logger.info("exe sql start");

		int[] r = db.batch(sqlList, sqlList.size());

		if (debug)
			logger.info("exe sql finish:" + DbTool.runTime(t));
		return r;

	}

	/**
	 * 批量提交
	 * 
	 * @param sql
	 * @return
	 */
	public int[] batchUpdate(String[] sqls) {
		List<String> sqlList = new ArrayList<String>();
		for (int i = 0; i < sqls.length; i++) {
			sqlList.add(sqls[i]);
		}
		return batchUpdate(sqlList);
	}

	/**
	 * 执行sql
	 * 
	 * @param sql
	 * @return
	 */
	public void query(String sql) {
		double t = DbTool.getTime();
		if (debug)
			logger.info("exe sql start" + DbTool.debugSql(sql));
		db.query(sql);
		if (debug)
			logger.info("exe sql finish:" + DbTool.runTime(t));
	}

	/**
	 * 获取数量--带参数
	 * 
	 * @param sql
	 * @param param
	 * @return @
	 */
	public long queryLong(String sql, Object... param) {

		double t = DbTool.getTime();
		if (debug)
			logger.info("exe sql start" + DbTool.debugSql(sql, param));
		long r = db.queryLong(sql, param);
		if (debug)
			logger.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 获取数量--带参数
	 * 
	 * @param sql
	 * @return
	 */
	public long queryLong(String sql, JSONArray param) {
		return queryLong(sql, param.toArray());
	}

	/**
	 * 获取数量--带参数
	 * 
	 * @param sql
	 * @param param
	 * @return @
	 */
	public int queryInt(String sql, Object... param) {

		double t = DbTool.getTime();
		if (debug)
			logger.info("exe sql start" + DbTool.debugSql(sql, param));
		int r = db.queryInt(sql, param);
		if (debug)
			logger.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 获取数量--带参数
	 * 
	 * @param sql
	 * @return
	 */
	public int queryInt(String sql, JSONArray param) {
		return queryInt(sql, param.toArray());
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
	 * @return @
	 */
	public long getSequenceNextval(String sName) {
		return db.queryLong("SELECT " + sName + ".nextval FROM dual");
	}

	/**
	 * 获取当前SEQUENCE val
	 * 
	 * @param sName
	 * @return @
	 */
	public long getSequenceCurrval(String sName) {
		return db.queryLong("SELECT " + sName + ".currval FROM dual");
	}

	/**
	 * 统计数量--带参数
	 * 
	 * @param sql
	 * @param param
	 * @return @
	 */
	public int count(String sql, JSONArray param) {
		return count(sql, param.toArray());
	}

	/**
	 * 统计数量--带参数
	 * 
	 * @param sql
	 * @param param
	 * @return @
	 */
	public int count(String sql, Object... param) {
		double t = DbTool.getTime();
		sql = replaceOrderBy(sql);

		if (isMySQL() || isSQLite()) {
			if (!isContainSubQuery(sql)) {
				sql = addMysqlCountStr(sql);
				if (debug)
					logger.info("exe sql start" + DbTool.debugSql(sql, param));

				final String _sql = sql;
				final List<Integer> result = new ArrayList<Integer>();
				db.tx(new IAtom() {
					@Override
					public boolean run() throws SQLException {
						db.query(_sql, param);
						String sql = "SELECT FOUND_ROWS()";
						result.add(db.queryInt(sql));
						return true;
					}

				});
				if (debug)
					logger.info("exe sql finish:" + DbTool.runTime(t));
				return result.size() == 1 ? result.get(0) : 0;
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
			logger.info("exe sql start" + DbTool.debugSql(sql, param));

		int r = db.queryInt(sql, param);
		// System.out.println(r);
		if (debug)
			logger.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 分页列表--带参数(外层自己计算分页)
	 * 
	 * @param sql
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public JSONArray queryList(String sql, int pageSize, int curPage, JSONArray param) {
		return queryList(sql, pageSize, curPage, param.toArray());
	}

	/**
	 * 分页列表--带参数(外层自己计算分页)
	 * 
	 * @param sql
	 * @param param
	 * @param pageSize
	 * @param curPage
	 * @return @
	 */
	public JSONArray queryList(String sql, int pageSize, int curPage, Object... param) {
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
			logger.info("exe sql start" + DbTool.debugSql(sql, param));

		JSONArray r = field2json(db.find(sql, param));
		if (debug)
			logger.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 分页列表--带参数(统计后返回页数)
	 * 
	 * @param sql
	 * @param pageSize
	 * @param curPage
	 * @param param
	 * @return @
	 */
	public JSONObject queryPageList(String sql, int pageSize, int curPage, JSONArray param) {
		return queryPageList(sql, pageSize, curPage, param.toArray());
	}

	/**
	 * 分页列表--带参数(统计后返回页数)
	 * 
	 * @param sql
	 * @param pageSize
	 * @param curPage
	 * @param param
	 * @return @
	 */
	public JSONObject queryPageList(String sql, int pageSize, int curPage, Object... param) {
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
				logger.info("exe sql start" + DbTool.debugSql(sql, param));
			r = field2json(db.find(sql, param));
			if (debug)
				logger.info("exe sql finish:" + DbTool.runTime(t));
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
	 * 列表，限制数量--带参数
	 * 
	 * @param sql
	 * @param num
	 * @return
	 */
	public JSONArray queryList(String sql, int num, JSONArray param) {
		return queryList(sql, num, param.toArray());
	}

	/**
	 * 列表，限制数量--带参数
	 * 
	 * @param sql
	 * @param param
	 * @param num
	 * @return @
	 */
	public JSONArray queryList(String sql, int num, Object... param) {
		if (isMySQL() || isSQLite()) {
			sql = sql + " limit ? ";
		} else {
			sql = "select * from (" + sql + ") where rownum <= ? ";
		}
		param = ArrayUtil.append(param, num);
		double t = DbTool.getTime();
		if (debug)
			logger.info("exe sql start" + DbTool.debugSql(sql, param));

		JSONArray r = field2json(db.find(sql, param));
		if (debug)
			logger.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 查询列表--带参数
	 * 
	 * @param sql
	 * @return
	 */
	public JSONArray queryList(String sql, JSONArray param) {
		return queryList(sql, param.toArray());
	}

	/**
	 * 查询列表--带参数
	 * 
	 * @param sql
	 * @param param
	 * @return @
	 */
	public JSONArray queryList(String sql, Object... param) {
		double t = DbTool.getTime();
		if (debug)
			logger.info("exe sql start" + DbTool.debugSql(sql, param));
		JSONArray r = field2json(db.find(sql, param));
		if (debug)
			logger.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 查询单记录--带参数
	 * 
	 * @param sql
	 * @return
	 */
	public JSONObject queryMap(String sql, JSONArray param) {
		return queryMap(sql, param.toArray());
	}

	/**
	 * 查询单记录--带参数
	 * 
	 * @param sql
	 * @param param
	 * @return @
	 */
	public JSONObject queryMap(String sql, Object... param) {
		double t = DbTool.getTime();
		if (debug)
			logger.info("exe sql start" + DbTool.debugSql(sql, param));
		if (isMySQL() || isSQLite()) {
			sql = sql + " limit 1";
		} else {
			sql = "select * from (" + sql + ") where rownum <=1 ";
		}
		JSONObject r = field2json(db.findFirst(sql, param));

		if (debug)
			logger.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 列表，限制数量--带参数
	 * 
	 * @param sql
	 * @param num
	 * @return
	 */
	public JSONArray q_get_list(String tableName, String where, String field, String orderby, int num, JSONArray param) {
		return q_get_list(tableName, where, field, orderby, num, param == null ? new Object[0] : param.toArray());
	}

	/**
	 * 列表，限制数量--带参数
	 * 
	 * @param sql
	 * @param param
	 * @param num
	 * @return @
	 */
	public JSONArray q_get_list(String tableName, String where, String field, String orderby, int num, Object... param) {
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

		return queryList(sql, num, param);
	}

	/**
	 * 查询单记录
	 * 
	 * @param sql
	 * @return
	 */
	public JSONObject q_get_map(String tableName, String where, String field, String orderby, JSONArray param) {
		return q_get_map(tableName, where, field, orderby, param == null ? new Object[0] : param.toArray());
	}

	/**
	 * 查询单记录
	 * 
	 * @param sql
	 * @param param
	 * @return @
	 */
	public JSONObject q_get_map(String tableName, String where, String field, String orderby, Object... param) {
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

		return queryMap(sql, param);
	}

	/**
	 * 删除记录
	 * 
	 * @param tbName
	 *                表名
	 * @param where
	 *                更新条件
	 * @param whereParams
	 *                更新条件值
	 * @return @
	 */
	public int q_del(String tbName, String where, JSONArray whereParams) {
		return q_del(tbName, where, whereParams != null ? whereParams.toArray() : new Object[] {});
	}

	/**
	 * 删除记录
	 * 
	 * @param tbName
	 *                表名
	 * @param where
	 *                更新条件
	 * @param whereParams
	 *                更新条件值
	 * @return @
	 */
	public int q_del(String tbName, String where, Object... whereParams) {
		JSONArray param = new JSONArray();
		StringBuffer sql = new StringBuffer();

		sql.append("delete from " + tbName + " where ");
		sql.append(where);

		if (whereParams != null && whereParams.length > 0) {
			for (Object whereParam : whereParams) {
				param.add(whereParam);
			}

		}
		return update(sql.toString(), param);
	}

	/**
	 * 插入表
	 * 
	 * @param tbName
	 *                表名
	 * @param map
	 *                字段及对应值
	 * @return 如果是MySQL则返回主键ID @
	 */
	public int q_insert(String tbName, JSONObject map) {
		return q_insert(tbName, map, true);
	}
	public int q_insert(String tbName, JSONObject map, boolean returnMysqlLastId) {

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
			logger.info("exe sql start" + DbTool.debugSql(sql.toString(), param));
		// int r = jtpl.update(sql.toString(), param.toArray());

		int r = 0;

		if(returnMysqlLastId && (isMySQL() || isSQLite())){// mysql返回 自增长ID

			final List<Integer> result = new ArrayList<Integer>();
			db.tx(new IAtom() {
				@Override
				public boolean run() throws SQLException {
					db.update(sql.toString(), param.toArray());
					String sql = "SELECT "+(isSQLite() ? "LAST_INSERT_ROWID" : "LAST_INSERT_ID")+"()";
					result.add(db.queryInt(sql));
					return true;
				}

			});
			r = result.size() == 1 ? result.get(0) : 0;
		} else {
			r = db.update(sql.toString(), param.toArray());
		}

		if (debug)
			logger.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 替换更新记录，MySQL专用
	 * 
	 * @param tbName
	 *                表名
	 * @param map
	 *                字段及对应值
	 * @return 如果是MySQL则返回主键ID @
	 */
	public int q_replace(String tbName, JSONObject map) {

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
			logger.info("exe sql start" + DbTool.debugSql(sql.toString(), param));
		// int r = jtpl.update(sql.toString(), param.toArray());

		int r = 0;

		if (isMySQL() || isSQLite()) {
			r = update(sql.toString(), param);
		} else {
			logger.error("(replace into table ...) only mysql");
		}

		if (debug)
			logger.info("exe sql finish:" + DbTool.runTime(t));
		return r;
	}

	/**
	 * 更新表
	 * 
	 * @param tbName
	 *                表名
	 * @param map
	 *                字段及对应值
	 * @param where
	 *                更新条件
	 * @param whereParams
	 *                更新条件值
	 * @return @
	 */
	public int q_update(String tbName, JSONObject map, String where, JSONArray whereParams) {
		return q_update(tbName, map, where, whereParams != null ? whereParams.toArray() : new Object[] {});
	}

	/**
	 * 更新表
	 * 
	 * @param tbName
	 *                表名
	 * @param map
	 *                字段及对应值
	 * @param where
	 *                更新条件
	 * @param whereParams
	 *                更新条件值
	 * @return @
	 */
	public int q_update(String tbName, JSONObject map, String where, Object... whereParams) {
		JSONArray param = new JSONArray();

		StringBuffer sqlKeys = new StringBuffer();

		Set<Entry<String, Object>> entrys = map.entrySet();
		String key;
		Object val;
		for (Entry<String, Object> entry : entrys) {
			key = entry.getKey();
			val = entry.getValue();

			if (val instanceof String && ((String) val).trim().toLowerCase().endsWith(".nextval")) {
				sqlKeys.append(key + "="+((String) val)+",");
			} else if (val instanceof StringBuffer) {
				sqlKeys.append(key + "="+((StringBuffer) val).toString()+",");
			} else{
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

		return update(sql.toString(), param);
	}

	public void test() {
		try {

			//System.out.println(JSON.toJSON(batchUpdate(new String[] { "insert into blog set title=\"77\", content=\"77\"", "insert into blog set title=\"99\", content=\"99\"" })));

			// List<Object[]> list = new ArrayList<Object[]>();
			// list.add(new Object[] { 11, 22 });
			// list.add(new Object[] { 33, 44 });
			// System.out.println(JSON.toJSON(batchUpdate("insert into blog set title=?, content=?", list)));

			//System.out.println(queryMap("select * from blog"));

			// System.out.println(count("select * from blog"));
			// System.out.println(JSON.toJSON(batchUpdate("insert into blog set title=?, content=?", new Object[][] { new Object[] { 11, 22 }, new Object[] { 33, 44 } })));
			// System.out.println(queryPageList("select * from blog", 10, 1));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		/*WebServer_PsDocManager.debug();
		DbExt db = new DbExt();
		db.setDebug(true);

		db.test();
		System.exit(0);*/
	}
}
