package za.co.zensar.cloud.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import org.springframework.stereotype.Service;

@Service
public class PostFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        System.out.println("Using Post Filter");

        return null;
    }

}