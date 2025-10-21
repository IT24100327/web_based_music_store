<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:set var="contextPath" value="${param.contextPath}"/>

<div class="modal fade" id="addUserModal" tabindex="-1" aria-labelledby="addUserModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addUserModalLabel">Add New User</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form id="addUserForm" action="${contextPath}/add-user" method="POST">
                <div class="modal-body">
                    <div class="form-group">
                        <label for="addFirstName">First Name</label>
                        <input type="text" class="form-control" id="addFirstName" name="firstName" required>
                    </div>
                    <div class="form-group">
                        <label for="addLastName">Last Name</label>
                        <input type="text" class="form-control" id="addLastName" name="lastName" required>
                    </div>
                    <div class="form-group">
                        <label for="addEmail">Email</label>
                        <input type="email" class="form-control" id="addEmail" name="email" required>
                    </div>
                    <div class="form-group">
                        <label for="addPassword">Password</label>
                        <input type="password" class="form-control" id="addPassword" name="password" required>
                    </div>
                    <div class="form-group">
                        <label for="addRole">Role</label>
                        <select class="form-control" id="addRole" name="role" required
                                onchange="toggleAdminRoleField('add')">
                            <option value="user">User</option>
                            <option value="admin">Admin</option>
                        </select>
                    </div>
                    <div class="form-group" id="addAdminRoleGroup" style="display: none;">
                        <label for="addAdminRole">Admin Role</label>
                        <select class="form-control" id="addAdminRole" name="adminRole">
                            <option value="">Select Admin Role</option>
                            <c:forEach var="role" items="${adminRoles}">
                                <option value="${role}">${role}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Add User</button>
                </div>
            </form>
        </div>
    </div>
</div>