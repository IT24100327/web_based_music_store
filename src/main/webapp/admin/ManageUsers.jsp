<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Hotel Admin - User Management</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/index.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-styles.css">
</head>
<body>
<div class="admin-container">
  <!-- Sidebar Navigation -->
  <jsp:include page="includes/admin_nav_bar.jsp">
    <jsp:param name="page" value="manageUsers"/>
  </jsp:include>

  <!-- Main Content Area -->
  <main class="admin-main">
    <header class="admin-header">
      <h1>User Management</h1>
      <div class="user-info">
        <div class="user-avatar">A</div>
        <div>
          <div>${sessionScope.USER.firstName} ${sessionScope.USER.lastName}</div>
          <div class="text-muted">${sessionScope.USER.email}</div>
        </div>
      </div>
    </header>

    <!-- Error Message Display -->
    <c:if test="${not empty error}">
      <div class="alert alert-danger"><c:out value="${error}"/></div>
    </c:if>

    <!-- Users Table -->
    <div class="table-container">
      <div class="table-header">
        <h3>All Users</h3>
        <div class="table-actions">
          <button class="btn btn-primary" onclick="openAddUserModal()">
            <i class="fas fa-plus"></i> Add User
          </button>
        </div>
      </div>
      <table>
        <thead>
        <tr>
          <th>ID</th>
          <th>User</th>
          <th>Email</th>
          <th>Role</th>
          <th>Status</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="user" items="${requestScope.allUsers}">
          <tr>
            <td><c:out value="${user.userId}"/></td>
            <td>
              <div><strong><c:out value="${user.firstName} ${user.lastName}"/></strong></div>
<%--              <div class="text-muted">Joined: 2023-10-15</div>--%>
            </td>
            <td><c:out value="${user.email}"/></td>
            <td>
              <span class="status-badge ${user.isAdmin() ? 'status-active' : 'status-inactive'}">
                <c:choose>
                  <c:when test="${user.isAdmin()}">
                    <c:choose>
                      <c:when test="${user.isAdmin()}">
                        Admin
                        <c:if test="${not empty user.role}">
                          (${user.role.roleName})
                        </c:if>
                      </c:when>
                      <c:otherwise>
                        User
                      </c:otherwise>
                    </c:choose>
                  </c:when>
                  <c:otherwise>
                    <c:out value="User"/>
                  </c:otherwise>
                </c:choose>
              </span>
            </td>
            <td>
              <span class="status-badge status-active">Active</span>
            </td>
            <td class="actions">
              <button class="btn btn-edit btn-sm" onclick="openEditModal('<c:out value="${user.userId}"/>', '<c:out value="${user.firstName}"/>', '<c:out value="${user.lastName}"/>', '<c:out value="${user.email}"/>', '<c:out value="${user.isAdmin()}"/>', '<c:out value="${user.isAdmin() && user.getRole() != null ? user.getRole().getRoleName() : ''}"/>')">
                <i class="fas fa-edit"></i> Edit
              </button>
              <button class="btn btn-delete btn-sm" onclick="openDeleteModal('<c:out value="${user.userId}"/>', '<c:out value="${user.firstName} ${user.lastName}"/>')">
                <i class="fas fa-trash"></i> Delete
              </button>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>

    <footer class="admin-footer">
      &copy; 2023 RhythmWave | Admin Panel
    </footer>
  </main>
</div>

<!-- Add User Modal -->
<jsp:include page="includes/modals/manage_user_modals/add_user_modal.jsp">
  <jsp:param name="contextPath" value="${pageContext.request.contextPath}"/>
</jsp:include>

<!-- Edit User Modal -->
<jsp:include page="includes/modals/manage_user_modals/edit_user_modal.jsp">
  <jsp:param name="contextPath" value="${pageContext.request.contextPath}"/>
</jsp:include>

<!-- Delete User Modal -->
<jsp:include page="includes/modals/manage_user_modals/delete_user_modal.jsp">
  <jsp:param name="contextPath" value="${pageContext.request.contextPath}"/>
</jsp:include>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
  // Initialize Bootstrap modals
  const addUserModal = new bootstrap.Modal(document.getElementById('addUserModal'));
  const editUserModal = new bootstrap.Modal(document.getElementById('editUserModal'));
  const deleteUserModal = new bootstrap.Modal(document.getElementById('deleteUserModal'));

  function openAddUserModal() {
    document.getElementById('addUserForm').reset();
    document.getElementById('addAdminRoleGroup').style.display = 'none';
    addUserModal.show();
  }

  function closeAddUserModal() {
    addUserModal.hide();
  }

  function openEditModal(userId, firstName, lastName, email, isAdmin, adminRole) {
    console.log('openEditModal called with:', userId, firstName, lastName, email, isAdmin, adminRole);
    document.getElementById('editUserId').value = userId;
    document.getElementById('editFirstName').value = firstName;
    document.getElementById('editLastName').value = lastName;
    document.getElementById('editEmail').value = email;
    document.getElementById('editRole').value = isAdmin === 'true' ? 'admin' : 'user';
    document.getElementById('editAdminRole').value = adminRole || '';
    document.getElementById('editAdminRoleGroup').style.display = isAdmin === 'true' ? 'block' : 'none';
    editUserModal.show();
  }

  function closeEditModal() {
    editUserModal.hide();
  }

  function openDeleteModal(userId, userName) {
    document.getElementById('deleteUserId').value = userId;
    document.getElementById('deleteUserName').textContent = userName;
    deleteUserModal.show();
  }

  function closeDeleteModal() {
    deleteUserModal.hide();
  }

  function toggleAdminRoleField(prefix) {
    const roleSelect = document.getElementById(prefix + 'Role');
    const adminRoleGroup = document.getElementById(prefix + 'AdminRoleGroup');
    adminRoleGroup.style.display = roleSelect.value === 'admin' ? 'block' : 'none';
  }

  // Client-side validation for forms
  document.getElementById('editUserForm').addEventListener('submit', function(event) {
    const role = document.getElementById('editRole').value;
    const adminRole = document.getElementById('editAdminRole').value;
    if (role === 'admin' && !adminRole) {
      event.preventDefault();
      alert('Please select an admin role.');
    }
  });

  document.getElementById('addUserForm').addEventListener('submit', function(event) {
    const role = document.getElementById('addRole').value;
    const adminRole = document.getElementById('addAdminRole').value;
    if (role === 'admin' && !adminRole) {
      event.preventDefault();
      alert('Please select an admin role.');
    }
  });
</script>
</body>
</html>