package com.tal.gateway.filter;

import com.tal.gateway.util.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
@Slf4j
public class LoginFilter implements GlobalFilter,Ordered {

    @Autowired
    private EncryptUtil encryptUtil;
    /**
     * 执行过滤器中的业务逻辑
     *     对请求参数中的access-token进行判断
     *      如果存在此参数:代表已经认证成功
     *      如果不存在此参数 : 认证失败.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {


        //1.获取请求参数access-token
        String token = exchange.getRequest().getHeaders().getFirst("token");

        //获取uri
        String uri = exchange.getRequest().getURI().toString();
        if(uri.contains("/note/findAll")||uri.contains("/note/register")||uri.contains("/note/login")){
            return chain.filter(exchange); //继续向下执行
        }else {
                //2.判断是否存在
                if(token == null || "".equals(token)) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }else {
                    //4.如果存在,继续执行
                    String s = "";
                    try{
                        s = encryptUtil.Base64Decode(token);
                    }catch (Exception e){
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }

                    String[] strings = s.split("_");
                    long exp = Long.parseLong(strings[1]);
                    long cur = System.currentTimeMillis();
                    System.out.println(cur+"  " +exp);
                    if(cur > exp){
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }else {
                        return chain.filter(exchange);
                    }

                }

        }

    }

    /**
     * 指定过滤器的执行顺序 , 返回值越小,执行优先级越高
     */
    @Override
    public int getOrder() {
        return 0;
    }
}