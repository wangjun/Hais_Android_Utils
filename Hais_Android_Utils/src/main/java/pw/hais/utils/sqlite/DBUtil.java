package pw.hais.utils.sqlite;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pw.hais.utils.L;
import pw.hais.utils.UtilConfig;


/**
 * SQLite数据库操作类
 *
 * @author 韦海生
 *         <p/>
 *         <p/>
 *         使用方法：
 *         1、在 Application 里 DBUtil.init(context);
 *         2、在 Model 类 的ID中 加上注解，auto设置自增，默认为自增
 *         /@Id(auto=true) private int id;
 *         3、不需要加入  数据库操作的这么 注解
 *         /@NoDB private int id;
 *         3、在需要使用的地方这么使用
 *         DBUtil.save(u);
 *         DBUtil.findById(User.class,"1");
 *         .....
 * @date 2014年7月15日
 * @date 2015年4月16日 更新为静态类
 */
public class DBUtil {
    private static final String TAG = "DBUtil";
    private static final String DB_NAME = UtilConfig.DB_NAME;    //数据库名
    private static final int DB_VERSION = UtilConfig.DB_VERSION;        //数据库版本号
    private static SQLiteDatabase db = null;   //数据库对象

    private static String tableName = "";        //实体类所对应的表名
    private static String tableId = "";            //实体类所对应的表主键
    private static List<Field> attr;         //实体类的属性
    private static String oldClassName;   //用于防止重复 获取信息


    private DBUtil() {
        throw new UnsupportedOperationException("请通过 DBUtil.init(context) 进行初始化");
    }


    /**
     * 保存数据
     *
     * @return 返回保存好的ID
     */
    public static int save(Object object) {
        initClassObject(object.getClass()); //初始化类内容
        L.i(TAG, "--" + tableName + "保存数据。");
        ContentValues cv = new ContentValues();
        try {
            for (Field field : attr) {
                if (!field.isAnnotationPresent(NoDB.class)) {    //判断该字段是否参与操作
                    String val = field.get(object) + "";    //获取值
                    if (!"null".equals(val) && !"0".equals(val)) { // 如果值不为空
                        cv.put(field.getName(), field.get(object) + "");    //把属性值添加到
                    }
                }
            }
            //开始写入数据库
            long row = db.insert(tableName, null, cv);
            return Integer.parseInt(row + "");    //返回相应条数
        } catch (Exception e) {
            L.e(TAG, "--" + tableName + "保存数据异常出错。", e);
        }
        return 0;
    }

    /**
     * 批量保存
     *
     * @param list
     * @return 修改条数
     */
    public static int saveList(List<?> list) {
        int x = 0;
        for (int i = 0; i < list.size(); i++) {
            int rs = save(list.get(i));
            x++;
        }
        return x;
    }

    /**
     * 根据ID删除内容
     *
     * @param id
     * @return 受影响的条数
     */
    public static int deleteById(Class clazz, String id) {
        initClassObject(clazz); //初始化类内容
        int row = 0;
        if (tableId.equals("")) L.e(TAG, "--该类无主键（唯一标示@ID），不能 deleteById");
        else {
            deleteByCondition(clazz, tableId + "=" + id);
        }
        return row;
    }

    /**
     * 根据条件删除 内容
     *
     * @param condition 条件
     * @return 受影响的条数
     */
    public static int deleteByCondition(Class clazz, String condition) {
        initClassObject(clazz); //初始化类内容
        if ("".equals(condition + "")) condition = " 1=1 ";
        L.i(TAG, "--" + tableName + "删除数据：" + String.valueOf(condition));
        try {
            return db.delete(tableName, condition, null); //执行删除
        } catch (Exception e) {
            L.e(TAG, "--" + tableName + "删除数据异常出错。", e);
        }
        return 0;
    }

    /**
     * 根据ID查询内容
     *
     * @param id
     * @return 单个内容，如匹配多个也是返回第一个
     */
    public static Object findById(Class clazz, String id) {
        initClassObject(clazz); //初始化类内容
        L.i(TAG, "--" + tableName + "查询数据：" + tableId + "=" + id);
        if (tableId.equals("")) L.e(TAG, "--该类无主键（唯一标示@ID），不能 findById");
        else {
            ArrayList<?> list = findEntityList(clazz, tableId + "=" + id, null);
            if (list != null && list.size() != 0) return list.get(0);
        }
        return null;
    }

    /**
     * 根据某项参数查询 内容
     *
     * @param condition 条件
     * @return 单个内容，如匹配多个也是返回第一个
     */
    public Object findByParame(Class clazz, String condition) {
        initClassObject(clazz); //初始化类内容
        L.i(TAG, "--" + tableName + "查询数据：" + condition);
        ArrayList<?> list = findEntityList(clazz, condition, null);
        if (list.size() != 0) return list.get(0);
        return null;
    }

    /**
     * 查询内容列表，排序
     *
     * @param conditions 条件
     * @param order      排序方式
     * @return 实体列表
     */
    public static ArrayList<?> findEntityList(Class clazz, String conditions, String order) {
        initClassObject(clazz); //初始化类内容

        if (null == conditions || "".equals(conditions + "")) conditions = " 1=1 ";
        if (null == order || "".equals(order)) order = "";
        try {
            ArrayList<Object> list = new ArrayList<Object>();
            Cursor c = db.query(tableName, null, conditions, null, null, null, order);//查询并获得游标
            while (c.moveToNext()) {
                Object o = clazz.newInstance();    //实例化一个类
                for (Field field : attr) {
                    if (!field.isAnnotationPresent(NoDB.class)) {    //判断该字段是否参与操作
                        //获取属性名， 首字母转为大写
                        String name = field.getName().replaceFirst(field.getName().substring(0, 1), field.getName().substring(0, 1).toUpperCase());
                        //根据数据类型调用方法
                        String typeName = field.getType().getName() + "";

                        int index = c.getColumnIndex(field.getName());    //根据 属性名 获取  数据库 index
                        if ("int".equals(typeName))field.set(o, c.getInt(index));
                        else if ("float".equals(typeName))field.set(o, c.getFloat(index));
                        else if ("double".equals(typeName))field.set(o, c.getDouble(index));
                        else if ("char".equals(typeName))field.set(o, c.getString(index));
                        else if ("long".equals(typeName))field.set(o, c.getLong(index));
                        else if ("blob".equals(typeName))field.set(o, c.getBlob(index));
                        else if ("java.lang.String".equals(typeName))field.set(o, c.getString(index));
                    }
                }
                list.add(o);
            }
            return list;
        } catch (Exception e) {
            L.e(TAG, "--查询 " + tableName + " " + conditions + "数据出现异常。", e);
        }
        return null;
    }

    /**
     * 根据主键修改数据
     *
     * @param object 实体内容
     * @return
     */
    public static boolean update(Object object) {
        initClassObject(object.getClass()); //初始化类内容
        if (tableId.equals("")) {
            L.i(TAG, "--根据主键修改数据失败，该表中无唯一标示（id），无法直接 update");
        } else {
            L.i(TAG, "--" + tableName + "修改数据。");
            try {
                //获取ID主键
                Field field = object.getClass().getDeclaredField(tableId);
                field.setAccessible(true);
                String value = field.get(object).toString();
                if (update(object, " " + tableId + " = '" + value + "'")) {
                    return true;
                }
            } catch (Exception e) {
                L.e(TAG, "--数据表" + tableName + "中根据主键修改数据出错。", e);
            }
        }
        return false;
    }

    /**
     * 根据条件修改内容
     *
     * @param conditions 条件
     * @return
     */
    public static boolean update(Object object, String conditions) {
        initClassObject(object.getClass()); //初始化类内容

        StringBuffer sql = new StringBuffer("update " + tableName + " set ");
        try {
            for (Field field : attr) {
                if (!field.isAnnotationPresent(NoDB.class)) {    //判断该字段是否参与操作
                    String val = field.get(object) + "";    //获取值
                    if (!"null".equals(val) && !"0".equals(val)) { // 如果值不为空
                        sql.append(field.getName() + "= '" + field.get(object) + "',");
                    }
                }
            }

            sql.deleteCharAt(sql.length() - 1); // 去掉最後一個逗號
            if (conditions != null && !"".equals(conditions)) {
                sql.append(" where " + conditions);
            }
            //获取操作
            db.execSQL(sql.toString());
            return true;
        } catch (Exception e) {
            L.e(TAG, "--数据库表" + tableName + "修改内容出错：" + sql, e);
        }
        return false;
    }

    /**
     * 根据    sql语句 执行 查询
     *
     * @param sql 完整sql语句
     * @return
     */
    public static Cursor executeQuery(String sql) {
        try {
            //执行操作
            Cursor c = db.rawQuery(sql, null);
            return c;
        } catch (Exception e) {
            L.i(TAG, "--执行查询语句出错。" + sql);
        }
        return null;
    }

    /**
     * 根据 sql语句 执行 更新语句
     *
     * @param sql 完整sql语句
     * @return
     */
    public static boolean executeUpdate(String sql) {
        try {
            db.execSQL(sql);
            return true;
        } catch (Exception e) {
            L.i(TAG, "--执行查询语句出错。" + sql);
        }
        return false;
    }


    /**
     * 自动创表
     */
    private static void createTable() {
        StringBuffer sql = new StringBuffer();
        if (!"".equals(tableName)) {
            try {
                //执行打开操作,判断是否此表已存在
                Cursor c = db.rawQuery("SELECT COUNT(*) FROM sqlite_master where type='table' and name='" + tableName + "'", null);
                c.moveToNext();    //下移标尺
                //判断表是否存在，不存在则创建
                if (c.getInt(0) == 0) {
                    //创建表
                    sql.append("create table " + tableName + " ( ");
                    for (Field field : attr) {
                        if (!field.isAnnotationPresent(NoDB.class)) {    //判断该字段是否创建数据库
                            String type = javaType2SQLType(field.getType().getName());    //获取当前的数据类型
                            if (field.getName().equals(tableId)) {
                                sql.append(field.getName() + " " + type + " primary key ");
                                if (field.isAnnotationPresent(Id.class))
                                    sql.append("autoincrement");
                                sql.append(",");
                            } else {
                                String defaultValue = "default ''";
                                if ("int".equals(type) || "float".equals(type) || "bigint".equals(type) || "date".equals(type))
                                    defaultValue = "default 0";            //当前的默认值
                                sql.append(field.getName() + " " + type + " not null " + defaultValue + ",");
                            }
                        }
                    }
                    sql.deleteCharAt(sql.length() - 1); // 去掉最後一個逗號
                    sql.append(")");

                    db.execSQL(sql.toString());    //执行sql语句
                    L.i(TAG, "--自動創表：" + tableName);
                }
            } catch (Exception e) {
                L.e(TAG, "--创表出现异常" + tableName + "." + sql, e);
            }
        }
    }


    /**
     * 初始化类
     *
     * @param clazz
     */
    private static void initClassObject(Class clazz) {
        if (oldClassName == null || !oldClassName.equals(clazz.getClass().getSimpleName())) {
            if (clazz == null) {
                L.e(TAG, "数据库操作发生错误，找不到Class：" + clazz);
            } else if (db == null) {
                L.e(TAG, "数据库操作发生错误，您未初始化数据库 DBUtil.init(context)");
            } else {
                tableName = clazz.getSimpleName();  //获取表名
                oldClassName = tableName;
                //添加属性
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true); // 设置属性为可访问
                    attr.add(field);
                    //获取id字段
                    if (field.isAnnotationPresent(Id.class)) tableId = field.getName();
                }
                if (tableId == null) tableId = "";
                createTable();
            }
        }
    }


    /**
     * Java数据类型 转换为 SQL数据类型
     *
     * @param type
     * @return
     */
    private static String javaType2SQLType(String type) {
        //int、float、double、char 不改变
        if ("java.lang.String".equals(type)) {
            type = "varchar(255)";
        } else if ("long".equals(type)) {
            type = "bigint";
        } else if ("int".equals(type)) {
            type = "INTEGER";
        }
        return type;
    }

    /**
     * 设置上下文，初始化数据库
     *
     * @param mContext
     */
    public static void init(Context mContext) {
        DatabaseHelper dbHelper = new DatabaseHelper(mContext, DB_NAME, null, DB_VERSION);
        db = dbHelper.getWritableDatabase();
        attr = new ArrayList<Field>();
    }


    /**
     * 数据操作基础类
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            L.i("DBHelper", "创建数据库" + DB_NAME);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            L.i("DBHelper", "更新数据库" + DB_NAME + "版本为" + DB_VERSION);
        }

    }
}

