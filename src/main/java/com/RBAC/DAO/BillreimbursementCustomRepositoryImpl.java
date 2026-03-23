package com.RBAC.DAO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.RBAC.Model.Employee;

public class BillreimbursementCustomRepositoryImpl implements BillreimbursementCustomRepository {
	
	 @PersistenceContext
	    private EntityManager entityManager;

		@Override
		public Object[] findSummary(Employee employee) {
			String queryString = "SELECT " +
	                "COUNT(CASE WHEN b.status = 'Approved' THEN 1 END), " + // Approved count
	                "COUNT(CASE WHEN b.status = 'Pending' THEN 1 END), " + // Pending count
	                "SUM(CASE WHEN b.status = 'Pending' THEN b.billAmount ELSE 0 END), " + // Total pending amount
	                "SUM(CASE WHEN b.status = 'Approved' THEN b.billAmount ELSE 0 END) " + // Total bill amount
	                "FROM Billreimbursement b " +
	                "WHERE b.employee = :employee";

	        Query query = entityManager.createQuery(queryString);
	        query.setParameter("employee", employee);
	        return (Object[]) query.getSingleResult();
	    }
		

}