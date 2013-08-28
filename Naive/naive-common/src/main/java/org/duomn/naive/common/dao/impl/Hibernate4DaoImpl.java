package org.duomn.naive.common.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.duomn.naive.common.dao.IBaseDao;
import org.duomn.naive.common.util.PageUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


public abstract class Hibernate4DaoImpl<M, PK extends Serializable> implements IBaseDao<M, PK> {
	
	private static final Logger logger = LoggerFactory.getLogger(Hibernate4DaoImpl.class);
	
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sf;
	
	private Class<?> entityClass;
	
	public Hibernate4DaoImpl(Class<?> entityClass) {
		this.entityClass = entityClass;
	}
	

	@Override
	public int countAll() {
		logger.debug("Count the sum of " + entityClass.getSimpleName());
		Query query = getSession().createQuery("select count(*) from " + entityClass.getSimpleName());
		return Integer.parseInt(query.uniqueResult().toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public M get(PK id) {
		logger.debug("Query the detail of by id [" + id + "]");
		return (M) getSession().get(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PK save(M model) {
		logger.debug("Save the object of " + entityClass.getSimpleName() + "[" + model + "]");
		return (PK) getSession().save(model);
	}

	@Override
	public void delete(PK id) {
		logger.debug("Delete the object by id [" + id + "]");
		getSession().delete(this.get(id));
	}

	@Override
	public void update(M model) {
		logger.debug("Update the object " + entityClass.getSimpleName() + "[" + model + "]");
		getSession().update(model);	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<M> listAll() {
		logger.debug("ListAll the " + entityClass.getSimpleName() + " object.");
		Query query = getSession().createQuery("from " + entityClass.getSimpleName());
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<M> listAll(int pn, int pageSize) {
		logger.debug("ListAll the " + entityClass.getSimpleName() + " object in pageIndex[" + pn + "] pageSize[" + pageSize + "]");
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
