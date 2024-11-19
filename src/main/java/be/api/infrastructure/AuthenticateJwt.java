package be.api.infrastructure;

import be.api.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticateJwt {
    public static int getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = new User();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if (userDetails instanceof User) {
                user = (User) userDetails;  // Chuyển về đối tượng User
                // Tiếp tục xử lý với đối tượng User
                return user.getUserId();
            } else {
                System.out.println("Authentication principal is not of type User.");
                return 0;  // hoặc xử lý trường hợp không phải User
            }

        }
        return 0;
    }
}
