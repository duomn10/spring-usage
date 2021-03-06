package com.duomn.common.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.duomn.common.dao.IBaseDao;
import com.duomn.util.PageUtils;

public abstract class Hibernate4DaoImpl<M, PK extends Serializable> implements IBaseDao<M, PK> {
	
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sf;
	
	private Class<?> entityClass;
	
	public Hibernate4DaoImpl(Class<?> entityClass) {
		this.entityClass = entityClass;
	}
	

	@Override
	public int countAll() {
		Query query = getSession().createQuery("select count(*) from " + entityClass.getSimpleName());
		return Integer.parseInt(query.uniqueResult().toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public M get(PK id) {
		return (M) getSession().get(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PK save(M model) {
		return (PK) getSession().save(model);
	}

	@Override
	public void delete(PK id) {
		getSession().delete(this.get(id));
	}

	@Override
	public void update(M model) {
		getSession().update(model);	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<M> listAll() {
		Query query = getSession().createQuery("from " + entityClass.getSimpleName());
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<M> listAll(int pn, int pageSize) {
		Query query = getSession().createQuery("from " + entityClass.getSimpleName());
		if (pn > 0 && pageSize > 0) {
			query.setMaxResults(pageSize);
			query.setFirstResult(PageUtils.getStartIndex(pn, pageSize));
		} else if (pn <= 0) { // 当所有参数没有处置时，查所有
			query.setFirstResult(0);
		}
		return query.list();
	}

	private Session getSession() {
		return sf.getCurrentSession();
	}
}
