package com.RBAC.DAO;

import com.RBAC.Model.Employee;

public interface BillreimbursementCustomRepository {

	Object[] findSummary(Employee employee);


}
