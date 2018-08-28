package test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import static com.dw.exercise.security.SecurityConstants.SECRET;

public class Test {
    public static void main(String[] args){
        String name = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImV4cCI6MTUzNjI4ODAxOX0.WZk1Edvf6D3fo4cF2EnUaB1R_MuBHxGYoy88THZk3W3FPyVN1TmHhQSIYIEkKT8vnEBwWmGaJAPF4LcJzPwztw")
                .getSubject();
        System.out.println(name);
    }
}
