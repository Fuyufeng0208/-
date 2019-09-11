package com.changgou.test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fyf on 2019/8/31
 */
public class JwtTest {
    /**
     * 创建jwt令牌
     */
    @Test
    public void testCreateJwt(){
        JwtBuilder builder = Jwts.builder()
                .setId("888")            //唯一编号
                .setSubject("小白")      //主题
                .setIssuedAt(new Date()) //签发日期
                .signWith(SignatureAlgorithm.HS256,"itcast");//设置签名，使用HS256算法

        //自定义数据
        Map<String,Object> userInfo = new HashMap<>();
        userInfo.put("username","张三丰");
        userInfo.put("age",188);
        userInfo.put("address","武当派");

        builder.addClaims(userInfo);
        System.out.println(builder.compact());
    }

    /**
     * 解析jwt令牌
     */
    @Test
    public void testParseJwt(){
        String compactJwt="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiLlsI_nmb0iLCJpYXQiOjE1NjcyMTQ0NzEsImFkZHJlc3MiOiLmrablvZPmtL4iLCJhZ2UiOjE4OCwidXNlcm5hbWUiOiLlvKDkuInkuLAifQ.25vUTcLH7DawmoEJCDEKkBFkayLal4NZdBHTyIcXNWM";
        Claims claims = Jwts.parser().
                setSigningKey("itcast").
                parseClaimsJws(compactJwt).
                getBody();
        System.out.println(claims);
    }
}
