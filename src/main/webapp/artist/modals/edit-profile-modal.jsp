<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<div class="modal fade" id="editProfileModal" tabindex="-1" aria-labelledby="editProfileModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content text-white" style="background-color: #1e1e1e;">
            <%-- Change the form action to point to the new servlet --%>
            <form action="${pageContext.request.contextPath}/artist/update-profile" method="POST">
                <div class="modal-header">
                    <h5 class="modal-title" id="editProfileModalLabel">Edit Your Profile</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                            aria-label="Close"></button>
                </div>

                <div class="modal-body">
                    <input type="hidden" name="userId" value="${sessionScope.USER.userId}">

                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="profileFirstName">First Name *</label>
                                <input type="text" class="form-control" id="profileFirstName" name="firstName"
                                       value="${sessionScope.USER.firstName}" required>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="profileLastName">Last Name *</label>
                                <input type="text" class="form-control" id="profileLastName" name="lastName"
                                       value="${sessionScope.USER.lastName}" required>
                            </div>
                        </div>
                    </div>

                    <div class="form-group mt-2">
                        <label for="profileEmail">Email *</label>
                        <input type="email" class="form-control" id="profileEmail" name="email"
                               value="${sessionScope.USER.email}" required>
                    </div>

                    <div class="form-group mt-2">
                        <label for="profileStageName">Stage Name *</label>
                        <input type="text" class="form-control" id="profileStageName" name="stageName"
                               value="${sessionScope.USER.stageName}" required>
                    </div>

                    <div class="form-group mt-2">
                        <label for="profilePassword">New Password</label>
                        <input type="password" class="form-control" id="profilePassword" name="editPassword"
                               placeholder="Leave blank to keep current password" minlength="4">
                    </div>

                    <div class="form-group mt-2">
                        <label for="profileBio">Your Bio</label>
                        <textarea class="form-control" id="profileBio" name="bio" rows="3"
                                  placeholder="Tell us about yourself..."
                                  maxlength="500">${sessionScope.USER.bio}</textarea>
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