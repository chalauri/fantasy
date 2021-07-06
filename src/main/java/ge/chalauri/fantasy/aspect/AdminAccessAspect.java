package ge.chalauri.fantasy.aspect;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

import ge.chalauri.fantasy.aspect.annotations.HasRole;
import ge.chalauri.fantasy.exceptions.ApiAccessException;
import ge.chalauri.fantasy.security.utils.SecurityConstants;
import ge.chalauri.fantasy.services.UserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;

@Aspect
@Configuration
public class AdminAccessAspect {

    private final UserService userService;

    public AdminAccessAspect(UserService userService) {
        this.userService = userService;
    }

    @Before("@annotation(ge.chalauri.fantasy.aspect.annotations.HasRole)")
    public void before(JoinPoint joinPoint) {
        UserDetails userDetails = userService.loadUserByUsername(SecurityConstants.LOGGED_IN_USERNAME);

        Set<String> roles = userDetails.getAuthorities().stream().map(Object::toString).collect(Collectors.toSet());

        String annotationValue = getAnnotationValue(joinPoint);

        if (!roles.contains(annotationValue)) {
            throw new ApiAccessException("You don't have enough rights to perform this operation");
        }
    }

    private String getAnnotationValue(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        HasRole annotation = method.getAnnotation(HasRole.class);
        return annotation.value().name();
    }

}
