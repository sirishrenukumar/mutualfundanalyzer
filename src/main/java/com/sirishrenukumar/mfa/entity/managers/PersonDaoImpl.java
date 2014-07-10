package com.sirishrenukumar.mfa.entity.managers;

import java.util.List;

import javax.annotation.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.sirishrenukumar.mfa.entity.Person;

/**
 *  * DAO implementation for Person entity  *   * @author DevCrumb.com  
 */
@ManagedBean
@Transactional
public class PersonDaoImpl {

	@PersistenceContext
	private EntityManager em;

	public Long save(Person person) {
		em.persist(person);
		return person.getId();
	}
	
	public List<Person>getAll() {
		return em.createQuery("SELECT p FROM Person p", Person.class).getResultList();
	}
	
}