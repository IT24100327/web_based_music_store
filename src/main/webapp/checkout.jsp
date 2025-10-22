<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment Successful | RhythmWave</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <style>
        .success-container {
            max-width: 800px;
            margin: 2rem auto;
            padding: 0 1rem;
        }
        
        .success-header {
            text-align: center;
            margin-bottom: 3rem;
        }
        
        .success-icon {
            font-size: 4rem;
            color: #28a745;
            margin-bottom: 1rem;
        }
        
        .success-header h1 {
            color: #fff;
            font-size: 2.5rem;
            margin-bottom: 0.5rem;
        }
        
        .success-header p {
            color: #ccc;
            font-size: 1.1rem;
        }
        
        .order-details {
            background: rgba(255, 255, 255, 0.1);
            border-radius: 15px;
            padding: 2rem;
            backdrop-filter: blur(10px);
            border: 1px solid rgba(255, 255, 255, 0.2);
            margin-bottom: 2rem;
        }
        
        .order-details h3 {
            color: #fff;
            margin-bottom: 1.5rem;
            font-size: 1.5rem;
        }
        
        .detail-row {
            display: flex;
            justify-content: space-between;
            padding: 0.5rem 0;
            border-bottom: 1px solid rgba(255, 255, 255, 0.1);
        }
        
        .detail-row:last-child {
            border-bottom: none;
        }
        
        .detail-label {
            color: #ccc;
            font-weight: 500;
        }
        
        .detail-value {
            color: #fff;
            font-weight: 600;
        }
        
        .purchased-items {
            background: rgba(255, 255, 255, 0.1);
            border-radius: 15px;
            padding: 2rem;
            backdrop-filter: blur(10px);
            border: 1px solid rgba(255, 255, 255, 0.2);
            margin-bottom: 2rem;
        }
        
        .purchased-items h3 {
            color: #fff;
            margin-bottom: 1.5rem;
            font-size: 1.5rem;
        }
        
        .item-card {
            display: flex;
            align-items: center;
            padding: 1rem;
            background: rgba(255, 255, 255, 0.05);
            border-radius: 10px;
            margin-bottom: 1rem;
            border: 1px solid rgba(255, 255, 255, 0.1);
        }
        
        .item-card:last-child {
            margin-bottom: 0;
        }
        
        .item-image {
            width: 80px;
            height: 80px;
            border-radius: 10px;
            margin-right: 1rem;
            object-fit: cover;
        }
        
        .item-info {
            flex: 1;
        }
        
        .item-info h5 {
            color: #fff;
            margin: 0 0 0.5rem 0;
            font-size: 1.1rem;
        }
        
        .item-info p {
            color: #ccc;
            margin: 0;
            font-size: 0.9rem;
        }
        
        .download-btn {
            background: linear-gradient(135deg, #28a745, #20c997);
            border: none;
            border-radius: 8px;
            color: #fff;
            padding: 0.5rem 1rem;
            font-size: 0.9rem;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
        }
        
        .download-btn:hover {
            background: linear-gradient(135deg, #20c997, #28a745);
            transform: translateY(-2px);
            color: #fff;
            text-decoration: none;
        }
        
        .action-buttons {
            display: flex;
            gap: 1rem;
            justify-content: center;
            margin-top: 2rem;
        }
        
        .btn-primary-custom {
            background: linear-gradient(135deg, #8a2be2, #9932cc);
            border: none;
            border-radius: 10px;
            color: #fff;
            padding: 1rem 2rem;
            font-size: 1.1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
        }
        
        .btn-primary-custom:hover {
            background: linear-gradient(135deg, #9932cc, #8a2be2);
            transform: translateY(-2px);
            color: #fff;
            text-decoration: none;
        }
        
        .btn-secondary-custom {
            background: rgba(255, 255, 255, 0.1);
            border: 2px solid rgba(255, 255, 255, 0.3);
            border-radius: 10px;
            color: #fff;
            padding: 1rem 2rem;
            font-size: 1.1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
        }
        
        .btn-secondary-custom:hover {
            background: rgba(255, 255, 255, 0.2);
            border-color: rgba(255, 255, 255, 0.5);
            color: #fff;
            text-decoration: none;
        }
        
        @media (max-width: 768px) {
            .action-buttons {
                flex-direction: column;
            }
            
            .item-card {
                flex-direction: column;
                text-align: center;
            }
            
            .item-image {
                margin-right: 0;
                margin-bottom: 1rem;
            }
        }
    </style>
</head>
<body>

<jsp:include page="/includes/navbar.jsp"/>

<main class="success-container">
    <div class="success-header">
        <div class="success-icon">
            <i class="fas fa-check-circle"></i>
        </div>
        <h1>Payment Successful!</h1>
        <p>Your order has been processed and your music is ready for download</p>
    </div>

    <div class="order-details">
        <h3><i class="fas fa-receipt me-2"></i>Order Details</h3>
        
        <div class="detail-row">
            <span class="detail-label">Order ID:</span>
            <span class="detail-value">#<c:out value="${order.orderId}"/></span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Order Date:</span>
            <span class="detail-value">
                <fmt:formatDate value="${order.orderDate}" pattern="MMM dd, yyyy 'at' HH:mm"/>
            </span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Payment Method:</span>
            <span class="detail-value">
                <c:choose>
                    <c:when test="${payment.paymentMethod == 'CARD'}">
                        <i class="fas fa-credit-card me-1"></i>Card Payment
                    </c:when>
                    <c:when test="${payment.paymentMethod == 'ONLINE'}">
                        <i class="fas fa-university me-1"></i>Online Banking
                    </c:when>
                    <c:otherwise>
                        <c:out value="${payment.paymentMethod}"/>
                    </c:otherwise>
                </c:choose>
            </span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Transaction ID:</span>
            <span class="detail-value"><c:out value="${payment.transactionId}"/></span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Total Amount:</span>
            <span class="detail-value">Rs. <fmt:formatNumber value="${order.totalAmount}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Status:</span>
            <span class="detail-value">
                <span style="color: #28a745; font-weight: 600;">
                    <i class="fas fa-check-circle me-1"></i>Completed
                </span>
            </span>
        </div>
    </div>

    <div class="purchased-items">
        <h3><i class="fas fa-music me-2"></i>Your Music</h3>
        
        <c:forEach var="item" items="${orderItems}">
            <div class="item-card">
                <img src="${pageContext.request.contextPath}/cover-art?trackId=${item.trackId}"
                     alt="Cover for <c:out value='${item.title}'/>" class="item-image">
                <div class="item-info">
                    <h5><c:out value="${item.title}"/></h5>
                    <p>by <c:out value="${item.artistName}"/></p>
                </div>
                <a href="${pageContext.request.contextPath}/download?trackId=${item.trackId}" 
                   class="download-btn">
                    <i class="fas fa-download me-1"></i>Download
                </a>
            </div>
        </c:forEach>
    </div>

    <div class="action-buttons">
        <a href="${pageContext.request.contextPath}/profile" class="btn-primary-custom">
            <i class="fas fa-list me-2"></i>View All Orders
        </a>
        <a href="${pageContext.request.contextPath}/" class="btn-secondary-custom">
            <i class="fas fa-home me-2"></i>Continue Shopping
        </a>
    </div>
</main>

<jsp:include page="/includes/footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
