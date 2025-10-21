<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<section class="welcome-section">
    <div class="welcome-icon"><i class="fas fa-user"></i></div>
    <div class="welcome-content">
        <h2>Profile Dashboard</h2>
        <p>Here's a quick overview of your account.</p>
    </div>
</section>
<div class="stats-grid">
    <div class="stat-card">
        <div class="stat-icon"><i class="fas fa-compact-disc"></i></div>
        <div class="stat-content">
            <h3>${fn:length(purchasedTracks)}</h3>
            <p>Tracks Purchased</p>
        </div>
    </div>
    <div class="stat-card">
        <div class="stat-icon"><i class="fas fa-history"></i></div>
        <div class="stat-content">
            <h3>${fn:length(userOrders)}</h3>
            <p>Total Orders</p>
        </div>
    </div>
</div>