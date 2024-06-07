package com.xsy.base.util;

import com.xsy.base.exception.ParamValidationException;
import org.junit.Assert;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ValidatorUtilsTest {

	@org.junit.Test
	public void test() {
		try {
			ValidatorUtils.validateEntity(new Test());
		} catch (ParamValidationException e) {
			System.out.println(e.getMessage());
			return;
		}
		Assert.fail();
	}

	@org.junit.Test
	public void test2() {
		Test t = new Test();
		t.a = 1;
		t.b = false;
		t.c = "1";
		ValidatorUtils.validateEntity(t);
	}

	public static class Test {
		@NotNull
		private Integer a;
		@NotNull
		private Boolean b;
		@NotBlank
		private String c;
	}
}