package pl.ark.chr.buginator.auth;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Component
public class CustomFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("HOOO");
        Data.setData("Hujek");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
