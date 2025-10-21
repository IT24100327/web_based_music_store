<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:set var="contextPath" value="${param.contextPath}"/>

<div class="modal fade" id="addPromotionModal" tabindex="-1" aria-labelledby="addPromotionModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addPromotionModalLabel">Add New Promotion</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form id="addPromotionForm" action="${contextPath}/add-promotion" method="POST">
                <div class="modal-body">
                    <input type="hidden" name="action" value="add">
                    <div class="form-group">
                        <label for="addPromoCode">Code</label>
                        <input type="text" class="form-control" id="addPromoCode" name="code" required>
                    </div>
                    <div class="form-group">
                        <label for="addDiscount">Discount</label>
                        <input type="number" class="form-control" id="addDiscount" name="discount" step="0.01" required>
                    </div>
                    <div class="form-group">
                        <label for="addStartDate">Start Date</label>
                        <input type="date" class="form-control" id="addStartDate" name="startDate" required>
                    </div>
                    <div class="form-group">
                        <label for="addEndDate">End Date</label>
                        <input type="date" class="form-control" id="addEndDate" name="endDate" required>
                    </div>
                    <div class="form-group">
                        <label for="addDescription">Description</label>
                        <input type="text" class="form-control" id="addDescription" name="description" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Add Promotion</button>
                </div>
            </form>
        </div>
    </div>
</div>