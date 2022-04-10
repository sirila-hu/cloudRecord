package com.example.top_sirilahu.util;

import com.alibaba.fastjson.JSON;
import com.example.top_sirilahu.entity.userEntity;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {

    private static String key;
    //token有效日期
    private static int expDay = 3;

    @Value("${jwt.key}")
    public void setKey(String key) {
        JWTUtil.key = key;
    }

    /**
     * - 创建jwt -
     * @param user 用户认证对象
     * @return 创建的token字符串
     */
    public static String createJWT(userEntity user) {
        //创建时间对象
        Calendar calendar = Calendar.getInstance();

        //生成jwtID用于唯一标识一次token创建
        String JWT_ID = String.valueOf(calendar.getTimeInMillis());

        //计算token到期时间戳
        calendar.add(Calendar.DAY_OF_MONTH, expDay);
        Date expiration = calendar.getTime();

        //转化user对象
        Map<String, Object> claims = new HashMap<>();
        claims.put("user", JSON.toJSONString(user));

        //创建并配置jwt对象
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setId(JWT_ID)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, key);

        return  builder.compact();
    }

    public static Claims parseJWT(String jwt) throws Exception {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(jwt).getBody();
            return claims;
        }catch (ExpiredJwtException expiredJwtException)
        {
            throw new Exception("凭证已过期，请重新登陆");
        }catch (UnsupportedJwtException unsupportedJwtException)
        {
            throw new Exception("凭证信息有误");
        }catch (MalformedJwtException malformedJwtException)
        {
            throw new Exception("密钥验证不一致");
        }catch (SignatureException signatureException)
        {
            throw new Exception("签名异常");
        }catch (IllegalArgumentException illegalArgumentException)
        {
            throw new Exception("含有非法参数");
        }
    }
}
