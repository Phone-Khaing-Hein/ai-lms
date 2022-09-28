package com.ai.controller;

import com.ai.entity.User;
import com.ai.entity.User.Role;
import com.ai.service.UserService;
import com.ai.util.UserNotFoundException;
import com.ai.util.Utility;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class SecurityController {

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/")
    public String home() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.Admin.name()))) {
            return "redirect:/admin/home";
        }
        if (auth != null && auth.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.Student.name()))) {
            return "redirect:/student/home";
        }
        if (auth != null && auth.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.Teacher.name()))) {
            return "redirect:/teacher/home";
        }
        return "redirect:/login";
    }

    //    -----------------------------Forgot Password--------------------------------------------------------

    @GetMapping("/forget-password")
    public ModelAndView forgot() {
        return new ModelAndView("FOG-PG001", "user", new User());
    }

    @PostMapping("/forget-password")
    public String forgetPasswordAction(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(45);
//        System.out.println(String.format("Email [%s] , Token [%s]",email,token));
        try {
            userService.updateResetPasswordToken(token, email);
            //generate reset password link based on the token
            String resetPasswordLink = Utility.getSiteUrl(request) + "/reset_password?token=" + token;
            //send email
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to email , Please Check!");
        } catch (UserNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error while sending email..");
        }
        return "FOG-PG001";
    }

    @GetMapping("/reset_password")
    public String resetPasswordForm(@Param(value = "token") String token, Model model) {
        User user = userService.get(token);
        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }
        model.addAttribute("token", token);

        return "NW-PG001";
    }

    @PostMapping("/reset_password")
    public String resetPasswordAction(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String newPassword = request.getParameter("nwpwd");
        User user = userService.get(token);
        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "NW-PG001";
        } else {
            userService.updatePassword(user, newPassword);
            model.addAttribute("message", "You have successfully changed your password!");
            return "LGN-PG001";
        }
    }


    private void sendEmail(String email, String resetPasswordLink) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("thetaungzan2022@gmail.com", "Ace Inspiration Support");
        helper.setTo(email);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>Your have requested to reset your password.</p>"
                + "<p>Click the link below to change your password</p>"
                + "<p><b><a href=\"" + resetPasswordLink + "\">Change My Password</a></b></p>"
                + "<p>Ignore this email if you do remember your password,OR you have not made the request.</p>";

        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

}
