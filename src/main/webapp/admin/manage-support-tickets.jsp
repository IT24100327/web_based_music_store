<%-- /webapp/admin/manage-support-tickets.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Manage Support Tickets</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/index.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-styles.css">
</head>
<body>
<div class="admin-container">
  <jsp:include page="includes/admin_nav_bar.jsp">
    <jsp:param name="page" value="manage-tracks"/>
  </jsp:include>

  <main class="admin-main">
    <header class="admin-header">
      <h1>Support Ticket Management</h1>
      <div class="user-avatar">A</div>
      <div>
        <div>${sessionScope.USER.firstName} ${sessionScope.USER.lastName}</div>
        <div class="text-muted">${sessionScope.USER.email}</div>
      </div>
    </header>

    <c:if test="${not empty param.success}">
      <div class.="alert alert-info">${param.success}</div>
    </c:if>

    <div class="stats-grid">
      <%-- Stat cards remain the same --%>
    </div>

    <div class="table-container">
      <div class="table-header">
        <h3>All Tickets</h3>
      </div>
      <table>
        <thead>
        <tr>
          <th>Ticket ID</th>
          <th>Subject</th>
          <th>User</th>
          <th>Status</th>
          <th>Priority</th>
          <th>Last Updated</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="ticket" items="${allTickets}">
          <tr>
            <td>#${ticket.ticketId}</td>
            <td>${ticket.subject}</td>
            <td>${ticket.userName}</td>
            <td><span class="status-badge status-${ticket.status}">${ticket.status}</span></td>
            <td>${ticket.priority}</td>
            <td><fmt:formatDate value="${ticket.lastUpdatedUtil}" pattern="MMM dd, yyyy"/></td>
            <td class="actions">
              <a href="${pageContext.request.contextPath}/SupportTicketServlet?action=viewTicket&ticketId=${ticket.ticketId}" class="btn btn-sm btn-secondary">
                <i class="fas fa-eye"></i> View
              </a>
              <c:if test="${ticket.status == 'closed'}">
                <form action="${pageContext.request.contextPath}/SupportTicketServlet" method="POST" onsubmit="return confirm('Are you sure you want to permanently delete this ticket?');" style="display: inline;">
                  <input type="hidden" name="action" value="deleteTicket">
                  <input type="hidden" name="ticketId" value="${ticket.ticketId}">
                  <button type="submit" class="btn btn-sm btn-delete">
                    <i class="fas fa-trash"></i> Delete
                  </button>
                </form>
              </c:if>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>
  </main>
</div>
</body>
</html>