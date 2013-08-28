package org.duomn.naive.example.dao;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.duomn.naive.example.entity.UserExample;
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
public class UserExampleDaoImplTest {

	AtomicInteger counter = new AtomicInteger();

	@Autowired
	private IUserExampleDao userDao;

	@Test
	public void testCount() {
		int before = userDao.countAll();
		for (int i = 0; i < 3; i++) {
			userDao.save(createUser());
		}
		int after = userDao.countAll();
		Assert.assertEquals(before + 3, after);

	}

	@Test
	public void testSave() {
		UserExample user = createUser();
		int id = userDao.save(user);

		UserExample saved = userDao.get(id);
		Assert.assertEquals(user, saved);
	}

	@Test
	public void testUpdate() {
		UserExample user = createUser();
		int id = userDao.save(user);
		String beforeName = userDao.get(id).getUsername();

		user.setUsername("Test");
		userDao.update(user);

		UserExample after = userDao.get(id);
		Assert.assertEquals(user, after);
		Assert.assertNotSame(beforeName, after.getUsername());

	}

	private UserExample createUser() {
		int random = counter.getAndAdd(1);
		UserExample user = new UserExample();
		user.setUsername("姓名" + random);
		user.setPassword("password" + random);
		user.setEmail("Email" + random);
		user.setRegisterDate(new Date());
		return user;
	}

}
