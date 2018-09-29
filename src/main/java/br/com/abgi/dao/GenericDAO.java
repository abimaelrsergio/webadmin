package br.com.abgi.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Abimael R. Sergio
 */
public abstract class GenericDAO<T> {

	public static final String PUNAME = "abgiPU";
		
	@Setter
	@Getter
	@PersistenceContext(name = PUNAME)
	private EntityManager em;
	private Class<T> classe;
	
	
	@SuppressWarnings("unchecked")
	public GenericDAO() {
		Type t = getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) t;
		classe = (Class<T>) pt.getActualTypeArguments()[0];
	}

	public T create(final T t) {
		this.getEm().persist(t);
		return t;
	}
	
	public T createAutoCommit(final T t) {
		this.getEm().getTransaction().begin();
		this.getEm().persist(t);
		this.getEm().getTransaction().commit();		
		return t;
	}

	public List<T> findAll() {
		final TypedQuery<T> query = this.getEm().createNamedQuery(String.format("%s.findAll", classe.getSimpleName()), classe);
		return query.getResultList();
	}

	public T find(final Long id) {
		T t = (T) this.getEm().find(classe, id);
		return t;
	}	
	
	public T update(final T t) {
		return this.getEm().merge(t);
	}
	
	public T updateAutoCommit(T t) {
		this.getEm().getTransaction().begin();
		t = this.getEm().merge(t);
		this.getEm().getTransaction().commit();	
		return t; 
	}
	
	public void delete(final Long id) {
		this.getEm().remove(this.getEm().getReference(classe, id));
	}

}
