package com.focusstudy.app.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile AppLimitDao _appLimitDao;

  private volatile UsageRecordDao _usageRecordDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `app_limits` (`packageName` TEXT NOT NULL, `appName` TEXT NOT NULL, `dailyLimitMinutes` INTEGER NOT NULL, `isEnabled` INTEGER NOT NULL, PRIMARY KEY(`packageName`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `usage_records` (`packageName` TEXT NOT NULL, `date` TEXT NOT NULL, `minutesUsed` INTEGER NOT NULL, `lastUpdatedMillis` INTEGER NOT NULL, PRIMARY KEY(`packageName`, `date`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '4fad4334686e5b17055bd8185681a7f7')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `app_limits`");
        db.execSQL("DROP TABLE IF EXISTS `usage_records`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsAppLimits = new HashMap<String, TableInfo.Column>(4);
        _columnsAppLimits.put("packageName", new TableInfo.Column("packageName", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppLimits.put("appName", new TableInfo.Column("appName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppLimits.put("dailyLimitMinutes", new TableInfo.Column("dailyLimitMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppLimits.put("isEnabled", new TableInfo.Column("isEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAppLimits = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAppLimits = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAppLimits = new TableInfo("app_limits", _columnsAppLimits, _foreignKeysAppLimits, _indicesAppLimits);
        final TableInfo _existingAppLimits = TableInfo.read(db, "app_limits");
        if (!_infoAppLimits.equals(_existingAppLimits)) {
          return new RoomOpenHelper.ValidationResult(false, "app_limits(com.focusstudy.app.data.model.AppLimit).\n"
                  + " Expected:\n" + _infoAppLimits + "\n"
                  + " Found:\n" + _existingAppLimits);
        }
        final HashMap<String, TableInfo.Column> _columnsUsageRecords = new HashMap<String, TableInfo.Column>(4);
        _columnsUsageRecords.put("packageName", new TableInfo.Column("packageName", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsageRecords.put("date", new TableInfo.Column("date", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsageRecords.put("minutesUsed", new TableInfo.Column("minutesUsed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsageRecords.put("lastUpdatedMillis", new TableInfo.Column("lastUpdatedMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsageRecords = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsageRecords = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsageRecords = new TableInfo("usage_records", _columnsUsageRecords, _foreignKeysUsageRecords, _indicesUsageRecords);
        final TableInfo _existingUsageRecords = TableInfo.read(db, "usage_records");
        if (!_infoUsageRecords.equals(_existingUsageRecords)) {
          return new RoomOpenHelper.ValidationResult(false, "usage_records(com.focusstudy.app.data.model.UsageRecord).\n"
                  + " Expected:\n" + _infoUsageRecords + "\n"
                  + " Found:\n" + _existingUsageRecords);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "4fad4334686e5b17055bd8185681a7f7", "9dde78c86b950c251759822419e0845b");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "app_limits","usage_records");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `app_limits`");
      _db.execSQL("DELETE FROM `usage_records`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(AppLimitDao.class, AppLimitDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UsageRecordDao.class, UsageRecordDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public AppLimitDao appLimitDao() {
    if (_appLimitDao != null) {
      return _appLimitDao;
    } else {
      synchronized(this) {
        if(_appLimitDao == null) {
          _appLimitDao = new AppLimitDao_Impl(this);
        }
        return _appLimitDao;
      }
    }
  }

  @Override
  public UsageRecordDao usageRecordDao() {
    if (_usageRecordDao != null) {
      return _usageRecordDao;
    } else {
      synchronized(this) {
        if(_usageRecordDao == null) {
          _usageRecordDao = new UsageRecordDao_Impl(this);
        }
        return _usageRecordDao;
      }
    }
  }
}
