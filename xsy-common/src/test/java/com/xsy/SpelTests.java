package com.xsy;

import com.xsy.security.enums.SecurityConstant;
import com.xsy.sys.entity.SysUserEntity;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author Q1sj
 * @date 2022.11.21 16:46
 */
public class SpelTests {
    @Test
    public void spel() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("('Hello' + ' World').concat(#end)");
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("end", "!");
        System.out.println(expression.getValue(context));
    }

    @Test
    public void spel2() {
        SpelExpressionParser spelParser = new SpelExpressionParser();
        Expression spel = spelParser.parseExpression("#user.getId()");
        EvaluationContext context = new StandardEvaluationContext();
        SysUserEntity user = new SysUserEntity();
        long id = 111L;
        user.setId(id);
        context.setVariable("user", user);
        Object value = spel.getValue(context);
        Assert.assertEquals(id, value);
    }
    
    @Test
    public void spel3() {
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression spel = parser.parseExpression("T(com.xsy.security.enums.SecurityConstant).getSysUserTokenCacheKey(#token)");
        StandardEvaluationContext context = new StandardEvaluationContext();
        String token = "a";
        context.setVariable("token", token);
        String value = spel.getValue(context, String.class);
        Assert.assertEquals(SecurityConstant.getSysUserTokenCacheKey(token), value);
    }
}
