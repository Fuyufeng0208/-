package com.changgou.token;

import org.junit.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

/*****
 * @Author: www.itheima
 * @Date: 2019/7/7 13:48
 * @Description: com.changgou.token
 *  使用公钥解密令牌数据
 ****/
public class ParseJwtTest {

    /***
     * 校验令牌
     */
    @Test
    public void testParseToken(){
        //令牌
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJhcHAiXSwibmFtZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTk5OTMxMjg3NywianRpIjoiMzQ5NzVjYjQtNmRmMC00Y2UzLTgzM2EtMzAyYWYxZTBlMmViIiwiY2xpZW50X2lkIjoiY2hhbmdnb3UiLCJ1c2VybmFtZSI6InN6aXRoZWltYSJ9.LhOOWZXaUihdiZKOpmKT_Hk1YtMoav6cjVO-QIYlyBXK99BSHLyc5Iz7Ujdk5zlgypr1QhQjTOe7OCX6vb4XOvhuauZSqUN9WAGjHi8uk7P3rvDjUlDU74u4mTOcaOQ90OLfbd83jPwN4TxNsrpMVy-hjetb7fy5nsPcnkSJQ3HbhH75WCVGvmCkACTqomTQJfgHSTjBuxrEbas1zpLo2Tm4vGn9ykELDRFWgm66fZ8GcPxOB_TXgqFeZiCAWNYkpjUKj7gHisXZbj9UFTmzLPLgyDMBpXxmkAWd6YGfpqtCrt6uTJz1nAO__IYTZz-OtY5FI27XmzwBAQe7rUK2yw";

        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuzFohgTocM9CX/Gbe0rEUJhkmFlueWe3VVEuvinOVLGZ4cvdgAqXGA5xCVW/svZz7D+4J1JqZxJpjaJnwmn8J3Vzpgikp7H6nfIVK1iiYzxqVN9Ngy7qiktlSOwTfdfFf7uGsqXl5mvXZnrdyseeizmRddKmcmz6UwuAjrw5KvhAbhVlw92ClVQcrfH2dacpDpyNWAbhpbsJhu3J+pUky6xTJEUG+/gv0zJAsNlVRe5fkl17b5iKcoQppaMjmSe6lRU6KJgV/LTlqYBOvQXgTuak3458IkjJzKPaSGhuJ8rGn6xUNAZthC+ApUBLIVw+vgYuP1MoY5g9e9pi3lp+6QIDAQAB-----END PUBLIC KEY-----";

        //校验Jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));

        //获取Jwt原始内容 载荷
        String claims = jwt.getClaims();
        System.out.println(claims);
        //jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }
}
