<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:set var="contextPath" value="${param.contextPath}"/>

<div class="modal fade" id="editPromotionModal" tabindex="-1" aria-labelledby="editPromotionModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editPromotionModalLabel">Edit Promotion</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form id="editPromotionForm" action="${contextPath}/update-promotion" method="POST">
                <div class="modal-body">
                    <input type="hidden" id="editPromotionId" name="promotionId">
                    <input type="hidden" name="action" value="update">
                    <div class="form-group">
                        <label for="editPromoCode">Code</label>
                        <input type="text" class="form-control" id="editPromoCode" name="code" required>
                    </div>
                    <div class="form-group">
                        <label for="editDiscount">Discount</label>
                        <input type="number" class="form-control" id="editDiscount" name="discount" step="0.01"
                               required>
                    </div>
                    <div class="form-group">
                        <label for="editStartDate">Start Date</label>
                        <input type="date" class="form-control" id="editStartDate" name="startDate" required>
                    </div>
                    <div class="form-group">
                        <label for="editEndDate">End Date</label>
                        <input type="date" class="form-control" id="editEndDate" name="endDate" required>
                    </div>
                    <div class="form-group">
                        <label for="editDescription">Description</label>
                        <input type="text" class="form-control" id="editDescription" name="description" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                </div>
            </form>
        </div>
    </div>
</div>