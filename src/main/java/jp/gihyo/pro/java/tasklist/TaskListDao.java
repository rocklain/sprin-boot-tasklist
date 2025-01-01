package jp.gihyo.pro.java.tasklist;

import jp.gihyo.pro.java.tasklist.HomeController.TaskItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class TaskListDao {
    // データベースとやり取りするためのJdbcTemplate
    private final JdbcTemplate jdbcTemplate;

    // コンストラクタでJdbcTemplateを依存性注入
    @Autowired
    TaskListDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // タスクをデータベースに追加
    public void add(TaskItem taskItem) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(taskItem);

        // SimpleJdbcInsert を利用してデータをINSERT
        SimpleJdbcInsert insert =
                new SimpleJdbcInsert(jdbcTemplate)
                        .withTableName("tasklist"); // 対象のテーブル名を指定
        insert.execute(param); // INSERT 実行
    }

    // データベースの全タスクを取得するメソッド
    public List<TaskItem> findAll() {
        String query = "SELECT * FROM tasklist";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);

        // DateTimeFormatter（必要ならカスタマイズ）
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<TaskItem> taskItems = result.stream()
                .map(row -> new TaskItem(
                        row.get("id").toString(),                    // id 列を文字列に変換
                        row.get("task").toString(),                  // task 列を文字列に変換
                        LocalDate.parse(row.get("deadline").toString(), formatter), // deadline を LocalDate に変換
                        (Boolean) row.get("done")                    // done 列を Boolean 型に変換
                ))
                .toList();

        return taskItems;
    }

    // 指定された ID のタスクを削除するメソッド
    public int delete(String id) {
        // DELETE 文を実行し、削除された行数を返す
        int number = jdbcTemplate.update("DELETE FROM tasklist WHERE id = ?", id);
        return number; // 削除された行数を返す
    }

    // 指定されたタスクを更新するメソッド
    public int update(TaskItem taskItem) {
        // UPDATE 文を実行し、更新された行数を返す
        int number = jdbcTemplate.update(
                "UPDATE tasklist SET task = ?, deadline = ?, done = ? WHERE id = ?",
                taskItem.task(),      // 新しいタスク名
                taskItem.deadline(),  // 新しい期限
                taskItem.done(),      // 新しい完了状態
                taskItem.id()         // 更新対象のタスク ID
        );
        return number; // 更新された行数を返す
    }
}
