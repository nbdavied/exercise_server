package test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

import static com.dw.exercise.security.SecurityConstants.SECRET;

public class Test {
    public static void main(String[] args){
        String name = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImV4cCI6MTUzNjI4ODAxOX0.WZk1Edvf6D3fo4cF2EnUaB1R_MuBHxGYoy88THZk3W3FPyVN1TmHhQSIYIEkKT8vnEBwWmGaJAPF4LcJzPwztw")
                .getSubject();
        System.out.println(name);
        List<String> authority = new ArrayList<>();
        //authority.stream().map(SimpleGrantedAuthority::new);
        authority.add("a");
        authority.add("ddd");
        authority.add("sdfa");
        System.out.println(authority.toString());

    }
}
