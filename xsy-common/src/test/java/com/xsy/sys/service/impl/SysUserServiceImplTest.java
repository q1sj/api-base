package com.xsy.sys.service.impl;

import com.xsy.base.exception.GlobalException;
import com.xsy.base.exception.UserLockedException;
import com.xsy.security.dto.LoginDTO;
import com.xsy.security.password.PasswordUtils;
import com.xsy.sys.dao.SysUserDao;
import com.xsy.sys.entity.SysUserEntity;
import com.xsy.sys.enums.UserStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

@Slf4j
public class SysUserServiceImplTest {
	@Spy
	@InjectMocks
	private SysUserServiceImpl sysUserService;
	@Mock
	private SysUserDao sysUserDao;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		SysUserEntity user = new SysUserEntity();
		user.setPassword(PasswordUtils.encode("ok"));
		user.setStatus(UserStatusEnum.ENABLED.value());
		Mockito.when(sysUserDao.getByUsername("user")).thenReturn(user);
	}

	@Test
	public void validLogin1() {
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setUsername("user");
		loginDTO.setPassword("ok");
		sysUserService.validLogin(loginDTO);
	}

	@Test
	public void validLogin2() {
		LoginDTO errorLoginDTO = new LoginDTO();
		errorLoginDTO.setUsername("user");
		errorLoginDTO.setPassword("ok2");
		Assert.assertThrows(GlobalException.class, () -> sysUserService.validLogin(errorLoginDTO));
		Assert.assertThrows(GlobalException.class, () -> sysUserService.validLogin(errorLoginDTO));
		Assert.assertThrows(GlobalException.class, () -> sysUserService.validLogin(errorLoginDTO));
		Assert.assertThrows(GlobalException.class, () -> sysUserService.validLogin(errorLoginDTO));
		Assert.assertThrows(GlobalException.class, () -> sysUserService.validLogin(errorLoginDTO));

		LoginDTO correctLoginDTO = new LoginDTO();
		correctLoginDTO.setUsername("user");
		correctLoginDTO.setPassword("ok");
		Assert.assertThrows(UserLockedException.class, () -> sysUserService.validLogin(correctLoginDTO));
	}

	@Test
	public void validLogin3() {
		LoginDTO errorLoginDTO = new LoginDTO();
		errorLoginDTO.setUsername("user");
		errorLoginDTO.setPassword("ok2");
		Assert.assertThrows(GlobalException.class, () -> sysUserService.validLogin(errorLoginDTO));
		Assert.assertThrows(GlobalException.class, () -> sysUserService.validLogin(errorLoginDTO));
		Assert.assertThrows(GlobalException.class, () -> sysUserService.validLogin(errorLoginDTO));
		Assert.assertThrows(GlobalException.class, () -> sysUserService.validLogin(errorLoginDTO));

		LoginDTO correctLoginDTO = new LoginDTO();
		correctLoginDTO.setUsername("user");
		correctLoginDTO.setPassword("ok");
		sysUserService.validLogin(correctLoginDTO);

		Assert.assertThrows(GlobalException.class, () -> sysUserService.validLogin(errorLoginDTO));
		sysUserService.validLogin(correctLoginDTO);
	}

}