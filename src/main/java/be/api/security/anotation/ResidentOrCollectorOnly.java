package be.api.security.anotation;

import org.springframework.security.access.prepost.PreAuthorize;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('ROLE_RESIDENT') or hasRole('ROLE_COLLECTOR')")
public @interface ResidentOrCollectorOnly {
}