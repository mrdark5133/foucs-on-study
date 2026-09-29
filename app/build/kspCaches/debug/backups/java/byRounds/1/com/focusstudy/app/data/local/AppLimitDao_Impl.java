package com.focusstudy.app.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.focusstudy.app.data.model.AppLimit;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppLimitDao_Impl implements AppLimitDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AppLimit> __insertionAdapterOfAppLimit;

  private final EntityInsertionAdapter<AppLimit> __insertionAdapterOfAppLimit_1;

  private final EntityDeletionOrUpdateAdapter<AppLimit> __updateAdapterOfAppLimit;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLimitValues;

  public AppLimitDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAppLimit = new EntityInsertionAdapter<AppLimit>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `app_limits` (`packageName`,`appName`,`dailyLimitMinutes`,`isEnabled`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AppLimit entity) {
        statement.bindString(1, entity.getPackageName());
        statement.bindString(2, entity.getAppName());
        statement.bindLong(3, entity.getDailyLimitMinutes());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(4, _tmp);
      }
    };
    this.__insertionAdapterOfAppLimit_1 = new EntityInsertionAdapter<AppLimit>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `app_limits` (`packageName`,`appName`,`dailyLimitMinutes`,`isEnabled`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AppLimit entity) {
        statement.bindString(1, entity.getPackageName());
        statement.bindString(2, entity.getAppName());
        statement.bindLong(3, entity.getDailyLimitMinutes());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(4, _tmp);
      }
    };
    this.__updateAdapterOfAppLimit = new EntityDeletionOrUpdateAdapter<AppLimit>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `app_limits` SET `packageName` = ?,`appName` = ?,`dailyLimitMinutes` = ?,`isEnabled` = ? WHERE `packageName` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AppLimit entity) {
        statement.bindString(1, entity.getPackageName());
        statement.bindString(2, entity.getAppName());
        statement.bindLong(3, entity.getDailyLimitMinutes());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindString(5, entity.getPackageName());
      }
    };
    this.__preparedStmtOfUpdateLimitValues = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE app_limits SET dailyLimitMinutes = ?, isEnabled = ? WHERE packageName = ?";
        return _query;
      }
    };
  }

  @Override
  public Object upsertLimit(final AppLimit limit, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAppLimit.insert(limit);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertLimitsIfNotExist(final List<AppLimit> limits,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAppLimit_1.insert(limits);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLimit(final AppLimit limit, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfAppLimit.handle(limit);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLimitValues(final String packageName, final int minutes,
      final boolean isEnabled, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLimitValues.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, minutes);
        _argIndex = 2;
        final int _tmp = isEnabled ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 3;
        _stmt.bindString(_argIndex, packageName);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateLimitValues.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AppLimit>> getAllLimitsFlow() {
    final String _sql = "SELECT * FROM app_limits";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"app_limits"}, new Callable<List<AppLimit>>() {
      @Override
      @NonNull
      public List<AppLimit> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfDailyLimitMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyLimitMinutes");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final List<AppLimit> _result = new ArrayList<AppLimit>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AppLimit _item;
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            final String _tmpAppName;
            _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            final int _tmpDailyLimitMinutes;
            _tmpDailyLimitMinutes = _cursor.getInt(_cursorIndexOfDailyLimitMinutes);
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            _item = new AppLimit(_tmpPackageName,_tmpAppName,_tmpDailyLimitMinutes,_tmpIsEnabled);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllLimits(final Continuation<? super List<AppLimit>> $completion) {
    final String _sql = "SELECT * FROM app_limits";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<AppLimit>>() {
      @Override
      @NonNull
      public List<AppLimit> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfDailyLimitMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyLimitMinutes");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final List<AppLimit> _result = new ArrayList<AppLimit>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AppLimit _item;
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            final String _tmpAppName;
            _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            final int _tmpDailyLimitMinutes;
            _tmpDailyLimitMinutes = _cursor.getInt(_cursorIndexOfDailyLimitMinutes);
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            _item = new AppLimit(_tmpPackageName,_tmpAppName,_tmpDailyLimitMinutes,_tmpIsEnabled);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getLimit(final String packageName,
      final Continuation<? super AppLimit> $completion) {
    final String _sql = "SELECT * FROM app_limits WHERE packageName = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, packageName);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AppLimit>() {
      @Override
      @Nullable
      public AppLimit call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfDailyLimitMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyLimitMinutes");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final AppLimit _result;
          if (_cursor.moveToFirst()) {
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            final String _tmpAppName;
            _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            final int _tmpDailyLimitMinutes;
            _tmpDailyLimitMinutes = _cursor.getInt(_cursorIndexOfDailyLimitMinutes);
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            _result = new AppLimit(_tmpPackageName,_tmpAppName,_tmpDailyLimitMinutes,_tmpIsEnabled);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<AppLimit> getLimitFlow(final String packageName) {
    final String _sql = "SELECT * FROM app_limits WHERE packageName = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, packageName);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"app_limits"}, new Callable<AppLimit>() {
      @Override
      @Nullable
      public AppLimit call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfDailyLimitMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyLimitMinutes");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final AppLimit _result;
          if (_cursor.moveToFirst()) {
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            final String _tmpAppName;
            _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            final int _tmpDailyLimitMinutes;
            _tmpDailyLimitMinutes = _cursor.getInt(_cursorIndexOfDailyLimitMinutes);
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            _result = new AppLimit(_tmpPackageName,_tmpAppName,_tmpDailyLimitMinutes,_tmpIsEnabled);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
