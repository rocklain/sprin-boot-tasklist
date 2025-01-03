package jp.gihyo.pro.java.tasklist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {
    // タスクアイテムのデータ構造
    record TaskItem(String id, String task, LocalDate deadline, boolean done) {}


    // タスクアイテムを保持するリスト
    private List<TaskItem> taskItems = new ArrayList<>();

    private final TaskListDao dao;

    @Autowired
    HomeController(TaskListDao dao){
        this.dao=dao;
    }

    @RequestMapping(value = "/hello")
    public String hello(Model model) {
        model.addAttribute("time", LocalDateTime.now());  // 時刻をモデルに追加
        return "hello";  // Thymeleafテンプレート「hello.html」を返す
    }

    @GetMapping("/list")
    public String listItems(@RequestParam(value = "sort", defaultValue = "") String sort,
                            @RequestParam(value = "order", defaultValue = "asc") String order,
                            Model model) {
        // findAll() の結果を ArrayList に変換
        List<TaskItem> taskItems = new ArrayList<>(dao.findAll());

        if (!sort.isEmpty() && !order.isEmpty()) {
            if (sort.equals("deadline")) {
                taskItems.sort((a, b) -> {
                    LocalDateTime deadlineA = (a.deadline() == null)
                            ? LocalDateTime.MIN
                            : a.deadline().atStartOfDay();
                    LocalDateTime deadlineB = (b.deadline() == null)
                            ? LocalDateTime.MIN
                            : b.deadline().atStartOfDay();
                    int compare = deadlineA.compareTo(deadlineB);
                    return order.equals("asc") ? compare : -compare;
                });
            } else if (sort.equals("done")) {
                taskItems.sort((a, b) -> {
                    int compare = Boolean.compare(a.done(), b.done());
                    return order.equals("asc") ? compare : -compare;
                });
            }
        }

        model.addAttribute("tasklist", taskItems);
        return "home";
    }



    @PostMapping("/add")
    public String addItem(@RequestParam("task") String task,
                          @RequestParam("deadline") String deadline,
                          Model model) {

        LocalDate parsedDeadline;
        try {
            parsedDeadline = LocalDate.parse(deadline); // String を LocalDate に変換
        } catch (Exception e) {
            model.addAttribute("errorMessage", "期限の日付形式が不正です。正しい形式で入力してください (例: 2025-01-01)。");
            return "error";
        }

        if (parsedDeadline.isBefore(LocalDate.now())) {
            model.addAttribute("errorMessage", "期限に過去の日付を登録することはできません。");
            return "error";
        }

        // IDを生成し、新しいタスクを追加
        String id = UUID.randomUUID().toString().substring(0, 8);
        TaskItem item = new TaskItem(id, task, parsedDeadline, false); // LocalDate 型を渡す
        dao.add(item);

        // リストページにリダイレクト
        return "redirect:/list";
    }


    @PostMapping("/delete")
    String deleteItem(@RequestParam("id") String id) {
        dao.delete(id);
        return "redirect:/list";
    }

    @PostMapping("/update")
    public String updateItem(@RequestParam("id") String id,
                             @RequestParam("task") String task,
                             @RequestParam("deadline") String deadline,
                             @RequestParam("done") boolean done) {

        LocalDate parsedDeadline;
        try {
            parsedDeadline = LocalDate.parse(deadline); // String を LocalDate に変換
        } catch (Exception e) {
            // 必要に応じてエラーハンドリングを追加
            return "error";
        }

        TaskItem taskItem = new TaskItem(id, task, parsedDeadline, done); // LocalDate 型を渡す
        dao.update(taskItem);
        return "redirect:/list";
    }
}

