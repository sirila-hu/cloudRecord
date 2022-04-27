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
    //acsess_token有效日期
    private static int expDay = 1;
    //refresh_token有效日期
    private static int refreshDay = 3;

    @Value("${jwt.key}")
    public void setKey(String key) {
        JWTUtil.key = key;
    }

    /**
     * - 创建access_token -
     * @param user 用户认证对象
     * @return 创建的token字符串
     */
    public static String createJWT(userEntity user) {

        //转化user对象
        Map<String, Object> claims = new HashMap<>();
        claims.put("user", JSON.toJSONString(user));

        //调用方法船舰jwt对象
        String token =  createJWT(false, claims);

        return  token;
    }

    /**
     * - 创建refresh_token -
     * @param UID 用户的UID
     * @return 创建的token字符串
     */
    public static String createRefresh(long UID)
    {
        //转化user对象
        Map<String, Object> claims = new HashMap<>();
        claims.put("UID", UID);

        //调用方法船舰jwt对象
        String token =  createJWT(true, claims);

        return  token;
    }

    /**
     * - 创建jwt核心方法 -
     * @param isRefresh 是否是refresh_token创建
     * @param claims 负载中填入的自定义信息
     * @return
     */
    private static String createJWT(boolean isRefresh, Map<String, Object> claims)
    {
        //创建时间对象
        Calendar calendar = Calendar.getInstance();

        //生成jwtID用于唯一标识一次token创建
        String JWT_ID = String.valueOf(calendar.getTimeInMillis());

        //计算token到期时间戳
        calendar.add(Calendar.DAY_OF_MONTH, isRefresh ? refreshDay : expDay);
//        calendar.set(Calendar.DAY_OF_MONTH, 15);
        Date expiration = calendar.getTime();

        //创建并配置jwt对象
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setId(JWT_ID)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, key);

        return  builder.compact();
    }

    /**
     * - 解析jwt -
     * @param jwt 待解析的jwt字符串
     * @return 解析成功后获得的用户信息
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(jwt).getBody();
            return claims;
        }catch (ExpiredJwtException expiredJwtException)
        {
            throw new ExpiredJwtException( expiredJwtException.getHeader(),expiredJwtException.getClaims(),"凭证已过期，请重新登陆");
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
