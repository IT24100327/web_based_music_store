<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hotel Admin - Marketing Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-styles.css">


</head>
<body>
<div class="admin-container">
    <!-- Sidebar Navigation -->
    <jsp:include page="includes/admin_nav_bar.jsp">
        <jsp:param name="page" value="manage-marketing"/>
    </jsp:include>

    <!-- Main Content Area -->
    <main class="admin-main">
        <header class="admin-header">
            <h1>Marketing Management</h1>
            <div class="user-info">
                <div class="user-avatar">A</div>
                <div>
                    <div>${sessionScope.USER.firstName} ${sessionScope.USER.lastName}</div>
                    <div class="text-muted">${sessionScope.USER.email}</div>
                </div>
            </div>
        </header>

        <!-- Welcome Section -->
        <section class="welcome-section">
            <div class="welcome-icon">
                <i class="fas fa-chart-bar"></i>
            </div>
            <div class="welcome-content">
                <h2>Marketing Management</h2>
                <p>Manage promotions, advertisements, and marketing campaigns for your platform.</p>
            </div>
        </section>

        <!-- Promotions Section -->
        <div class="table-container">
            <div class="table-header">
                <h3>Manage Promotions</h3>
                <div class="table-actions">
                    <button class="btn btn-secondary" onclick="openAddPromotionModal()">
                        <i class="fas fa-plus"></i> Add Promotion
                    </button>
                </div>
            </div>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Code</th>
                    <th>Discount</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Usage</th>
                    <th>Description</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty requestScope.allPromotions}">
                        <c:forEach var="promo" items="${requestScope.allPromotions}">
                            <tr>
                                <td>${promo.promotionId}</td>
                                <td><strong>${promo.code}</strong></td>
                                <td>${promo.discount}%</td>
                                <td>${promo.startDate}</td>
                                <td>${promo.endDate}</td>
                                <td>${promo.usageCount}</td>
                                <td>${promo.description}</td>
                                <td>
                                    <span class="status-badge status-active">Active</span>
                                </td>
                                <td class="actions">
                                    <button class="btn btn-edit btn-sm"
                                            onclick="openEditPromotionModal('${promo.promotionId}', '${promo.code}', '${promo.discount}', '${promo.startDate}', '${promo.endDate}', '${promo.description}')">
                                        <i class="fas fa-edit"></i>
                                    </button>
                                    <button class="btn btn-delete btn-sm"
                                            onclick="openDeletePromotionModal('${promo.promotionId}', '${promo.code}')">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="9" class="text-muted text-center py-4">No promotions found.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>

        <!-- Advertisements Section -->
        <div class="table-container">
            <div class="table-header">
                <h3>Manage Advertisements</h3>
                <div class="table-actions">
                    <button class="btn btn-secondary" onclick="openAddAdvertisementModal()">
                        <i class="fas fa-plus"></i> Add Advertisement
                    </button>
                </div>
            </div>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Content</th>
                    <th>Image</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty requestScope.allAdvertisements}">
                        <c:forEach var="ad" items="${requestScope.allAdvertisements}">
                            <tr>
                                <td>${ad.adId}</td>
                                <td><strong>${ad.title}</strong></td>
                                <td>${ad.content}</td>
                                <td>
                                    <c:if test="${not empty ad.imageData}">
                                        <img src="${pageContext.request.contextPath}/image?adId=${ad.adId}"
                                             class="preview-img" alt="Ad Image">
                                    </c:if>
                                </td>
                                <td>${ad.startDate}</td>
                                <td>${ad.endDate}</td>
                                <td>
                  <span class="status-badge ${ad.active ? 'status-active' : 'status-inactive'}">
                          ${ad.active ? 'Active' : 'Inactive'}
                  </span>
                                </td>
                                <td class="actions">
                                    <button class="btn btn-edit btn-sm"
                                            onclick="openEditAdvertisementModal('${ad.adId}', '${ad.title}', '${ad.content}', '${ad.imageUrl}', '${ad.startDate}', '${ad.endDate}', '${ad.active}')">
                                        <i class="fas fa-edit"></i>
                                    </button>
                                    <button class="btn btn-delete btn-sm"
                                            onclick="openDeleteAdvertisementModal('${ad.adId}', '${ad.title}')">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="8" class="text-muted text-center py-4">No advertisements found.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>

        <footer class="admin-footer">
            &copy; 2023 RhythmWave | Admin Panel
        </footer>
    </main>
</div>

<!-- Modals -->
<jsp:include page="includes/modals/manage-marketing-modals/add-promotion-modal.jsp">
    <jsp:param name="contextPath" value="${pageContext.request.contextPath}"/>
</jsp:include>
<jsp:include page="includes/modals/manage-marketing-modals/edit-promotion-modal.jsp">
    <jsp:param name="contextPath" value="${pageContext.request.contextPath}"/>
</jsp:include>
<jsp:include page="includes/modals/manage-marketing-modals/delete-promotion-modal.jsp">
    <jsp:param name="contextPath" value="${pageContext.request.contextPath}"/>
</jsp:include>
<jsp:include page="includes/modals/manage-marketing-modals/add-advertisement-modal.jsp">
    <jsp:param name="contextPath" value="${pageContext.request.contextPath}"/>
</jsp:include>
<jsp:include page="includes/modals/manage-marketing-modals/edit-advertisement-modal.jsp">
    <jsp:param name="contextPath" value="${pageContext.request.contextPath}"/>
</jsp:include>
<jsp:include page="includes/modals/manage-marketing-modals/delete-advertisement-modal.jsp">
    <jsp:param name="contextPath" value="${pageContext.request.contextPath}"/>
</jsp:include>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Initialize Bootstrap modals
    const addPromotionModal = new bootstrap.Modal(document.getElementById('addPromotionModal'));
    const editPromotionModal = new bootstrap.Modal(document.getElementById('editPromotionModal'));
    const deletePromotionModal = new bootstrap.Modal(document.getElementById('deletePromotionModal'));
    const addAdvertisementModal = new bootstrap.Modal(document.getElementById('addAdvertisementModal'));
    const editAdvertisementModal = new bootstrap.Modal(document.getElementById('editAdvertisementModal'));
    const deleteAdvertisementModal = new bootstrap.Modal(document.getElementById('deleteAdvertisementModal'));

    function openAddPromotionModal() {
        addPromotionModal.show();
    }

    function closeAddPromotionModal() {
        addPromotionModal.hide();
    }

    function openEditPromotionModal(promotionId, code, discount, startDate, endDate, description) {
        document.getElementById('editPromotionId').value = promotionId;
        document.getElementById('editPromoCode').value = code;
        document.getElementById('editDiscount').value = discount;
        document.getElementById('editStartDate').value = startDate;
        document.getElementById('editEndDate').value = endDate;
        document.getElementById('editDescription').value = description;
        editPromotionModal.show();
    }

    function closeEditPromotionModal() {
        editPromotionModal.hide();
    }

    function openDeletePromotionModal(promotionId, code) {
        document.getElementById('deletePromotionId').value = promotionId;
        document.getElementById('deletePromoCode').textContent = code;
        deletePromotionModal.show();
    }

    function closeDeletePromotionModal() {
        deletePromotionModal.hide();
    }

    function openAddAdvertisementModal() {
        addAdvertisementModal.show();
    }

    function closeAddAdvertisementModal() {
        addAdvertisementModal.hide();
    }

    function openEditAdvertisementModal(adId, title, content, imageUrl, startDate, endDate, active) {
        document.getElementById('editAdId').value = adId;
        document.getElementById('editAdTitle').value = title;
        document.getElementById('editAdContent').value = content;
        document.getElementById('editAdStartDate').value = startDate;
        document.getElementById('editAdEndDate').value = endDate;
        document.getElementById('editAdActive').value = active;

        // Load current image preview
        const previewImg = document.getElementById('editAdPreview');
        const contextPath = '${pageContext.request.contextPath}'; // Assumes JSP context available
        if (adId) {
            previewImg.src = contextPath + '/image?adId=' + adId;
            previewImg.style.display = 'block'; // Show preview
        } else {
            previewImg.style.display = 'none';
        }

        editAdvertisementModal.show();
    }

    function closeEditAdvertisementModal() {
        editAdvertisementModal.hide();
    }

    function openDeleteAdvertisementModal(adId, title) {
        document.getElementById('deleteAdId').value = adId;
        document.getElementById('deleteAdTitle').textContent = title;
        deleteAdvertisementModal.show();
    }

    function closeDeleteAdvertisementModal() {
        deleteAdvertisementModal.hide();
    }
</script>
</body>
</html>