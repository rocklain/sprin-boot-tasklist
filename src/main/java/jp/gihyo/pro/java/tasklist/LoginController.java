package jp.gihyo.pro.java.tasklist;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    // ルートパスにアクセスした際に /login にリダイレクト
    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login"; // 修正: スラッシュを追加
    }

    // ログインページの表示
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // "login.html" を表示
    }

    // ログイン処理
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
