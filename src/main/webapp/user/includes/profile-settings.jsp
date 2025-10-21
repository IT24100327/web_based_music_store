<%-- /WEB-INF/views/profile-settings.jsp --%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<div class="table-container">
    <div class="table-header"><h3>Account Settings</h3></div>
    <c:if test="${not empty param.error}">
        <div class="alert alert-danger">${param.error}</div>
    </c:if>
    <c:if test="${not empty param.success}">
        <div class="alert alert-success">${param.success}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/update-profile" method="POST">
        <div class="row">
            <div class="col-md-6 mb-3">
                <label for="firstName" class="form-label">First Name</label>
                <input type="text" class="form-control" id="firstName" name="firstName"
                       value="${sessionScope.USER.firstName}" required>
            </div>
            <div class="col-md-6 mb-3">
                <label for="lastName" class="form-label">Last Name</label>
                <input type="text" class="form-control" id="lastName" name="lastName"
                       value="${sessionScope.USER.lastName}" required>
            </div>
        </div>
        <div class="mb-3">
            <label for="email" class="form-label">Email Address</label>
            <input type="email" class="form-control" id="email" name="email" value="${sessionScope.USER.email}"
                   required>
        </div>
        <div class="mb-3">
            <label for="newPassword" class="form-label">New Password</label>
            <input type="password" class="form-control" id="newPassword" name="newPassword"
                   placeholder="Leave blank to keep current password">
        </div>
        <button type="submit" class="btn btn-primary">Save Changes</button>
    </form>
</div>