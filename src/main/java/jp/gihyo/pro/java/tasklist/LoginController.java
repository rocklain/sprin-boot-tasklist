package jp.gihyo.pro.java.tasklist;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // ログインページの表示
    }

    @PostMapping("/login")
    public String handleLogin(
            @RequestParam String username,
            @RequestParam String password,
            Model model) {
        if ("sample".equals(username) && "samplepass".equals(password)) {
            return "redirect:/list"; // ログイン成功時にリストページへリダイレクト
        } else {
            model.addAttribute("error", "ユーザーIDまたはパスワードが間違っています。");
            return "login"; // ログイン失敗時は再度ログインページを表示
        }
    }
}
