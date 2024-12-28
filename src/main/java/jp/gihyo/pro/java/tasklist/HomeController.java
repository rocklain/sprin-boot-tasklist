package jp.gihyo.pro.java.tasklist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {
    // タスクアイテムのデータ構造
    record TaskItem(String id, String task, String deadline, boolean done) {}

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
    public String listItems(Model model) {
        // モデルにリストを追加
        List<TaskItem> taskItems = dao.findAll();
        model.addAttribute("tasklist", taskItems); // HTMLテンプレートの名前に合わせる
        return "home"; // `home.html` をレンダリング
    }

    @GetMapping("/add")
    public String addItem(@RequestParam("task") String task,
                          @RequestParam("deadline") String deadline) {
        // IDを生成し、新しいタスクを追加
        String id = UUID.randomUUID().toString().substring(0, 8);
        TaskItem item = new TaskItem(id, task, deadline, false);
        dao.add(item);

        // リストページにリダイレクト
        return "redirect:/list";
    }

    @GetMapping("/delete")
    String deleteItem(@RequestParam("id") String id){
        dao.delete(id);
        return "redirect:/list";
    }

    @GetMapping("/update")
    String updateItem(@RequestParam("id") String id,
                      @RequestParam("task") String task,
                      @RequestParam("deadline")String deadline,
                      @RequestParam("done") boolean done) {
        TaskItem taskItem = new TaskItem(id,task,deadline,done);
        dao.update(taskItem);
        return "redirect:/list";
    }
}
