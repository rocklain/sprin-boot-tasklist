package jp.gihyo.pro.java.tasklist;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class HomeRestController {

    // タスクアイテムのデータ構造を定義
    record TaskItem(String id, String task, String deadline, boolean done) {
    }

    // タスクアイテムを保持するリスト
    private List<TaskItem> taskItems = new ArrayList<>();

    // 「/resthello」のエンドポイント
    @GetMapping("/resthello")
    public String hello() {
        return """
                Hello.
                It works!
                現在時刻は%sです。
                """.formatted(LocalDateTime.now());
    }

    // 「/restadd」のエンドポイント
    @GetMapping("/restadd")
    public String addItem(@RequestParam("task") String task,
                          @RequestParam("deadline") String deadline) {
        // ランダムなIDを生成
        String id = UUID.randomUUID().toString().substring(0, 8);

        // 新しいタスクアイテムを作成
        TaskItem item = new TaskItem(id, task, deadline, false);

        // リストに追加
        taskItems.add(item);

        // レスポンスを返す
        return "タスクを追加しました。";

    }

    @GetMapping("/restlist")
    String listItems(){
        String result = taskItems.stream()
                .map(TaskItem::toString)
                .collect(Collectors.joining(","));
        return result;
    }
}
