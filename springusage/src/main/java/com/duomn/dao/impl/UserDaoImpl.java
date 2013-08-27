package com.duomn.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.duomn.dao.IUserDao;
import com.duomn.entity.UserModel;

@Repository
public class UserDaoImpl implements IUserDao {
	
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sf;

	@Override
	public int countAll() {
		Query query = getSession().createQuery("select count(*) from UserModel");
		return (Integer) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserModel> listAll() {
		Query query = getSession().createQuery("from UserModel");
		return query.list();
	}
	
	private Session getSession() {
		return sf.getCurrentSession();
	}
	
}
