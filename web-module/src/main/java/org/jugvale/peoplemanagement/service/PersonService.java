package org.jugvale.peoplemanagement.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jugvale.peoplemanagement.model.Person;

@Stateless
@Default
public class PersonService {

	@PersistenceContext(unitName = "primary")
	protected EntityManager em;

	public void save(Person p) {
		em.persist(p);
	}

	public void remove(Person p) {
		Person toRemove = findById(p.getId());
		em.remove(toRemove);
	}

	public List<Person> listAll() {
		CriteriaQuery<Person> cq = em.getCriteriaBuilder().createQuery(
				Person.class);
		cq.select(cq.from(Person.class));
		return em.createQuery(cq).getResultList();
	}

	public Person findById(long id) {
		return em.find(Person.class, id);
	}

	public Person findByRFID(String rfid) {
		CriteriaQuery<Person> cq = em.getCriteriaBuilder().createQuery(
				Person.class);
		Root<Person> person = cq.from(Person.class);
		cq.where(person.get("rfid").in(rfid));
		Person result;
		try {
			result = em.createQuery(cq).getSingleResult();
		} catch (NoResultException e) {
			result = null;
		}
		return result;
	}

	public Person update(Person p) {
		return em.merge(p);
	}
}