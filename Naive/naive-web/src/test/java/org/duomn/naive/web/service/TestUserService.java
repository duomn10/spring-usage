package org.duomn.naive.web.service;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.duomn.naive.web.entity.UserModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@Transactional
@TransactionConfiguration(transactionManager = "hibernateTx", defaultRollback = true)
public class TestUserService {
	AtomicInteger counter = new AtomicInteger();

	@Autowired
	private IUserService userService;

	@Test
	public void testCount() {
		int before = userService.count();
		for (int i = 0; i < 3; i++) {
			userService.save(createUser());
		}
		int after = userService.count();
		Assert.assertEquals(before + 3, after);

	}

	@Test
	public void testSave() {
		UserModel user = createUser();
		int id = userService.save(user);

		UserModel saved = userService.get(id);
		Assert.assertEquals(user, saved);
	}

	@Test
	public void testUpdate() {
		UserModel user = createUser();
		int id = userService.save(user);
		String beforeName = userService.get(id).getUsername();

		user.setUsername("Test");
		userService.update(user);

		UserModel after = userService.get(id);
		Assert.assertEquals(user, after);
		Assert.assertNotSame(beforeName, after.getUsername());

	}

	private UserModel createUser() {
		int random = counter.getAndAdd(1);
		UserModel user = new UserModel();
		user.setUsername("姓名" + random);
		user.setPassword("password" + random);
		user.setEmail("Email" + random);
		user.setRegisterDate(new Date());
		return user;
	}

}
