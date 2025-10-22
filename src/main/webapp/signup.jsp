<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RhythmWave - Sign Up</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
    <style>
        .invalid-feedback {
            display: block;
            color: #dc3545;
            font-size: 0.875em;
            margin-top: 0.25rem;
        }

        .is-invalid {
            border-color: #dc3545;
        }

        .is-valid {
            border-color: #198754;
        }

        .user-type-options {
            display: flex;
            gap: 1rem;
            margin-bottom: 1rem;
        }

        .user-type-option {
            flex: 1;
            text-align: center;
            padding: 1rem;
            border: 2px solid #444;
            border-radius: 8px;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .user-type-option:hover {
            border-color: #bb86fc;
        }

        .user-type-option.selected {
            border-color: #bb86fc;
            background-color: rgba(187, 134, 252, 0.1);
        }

        .user-type-option input {
            display: none;
        }

        /* Horizontal form layout */
        .form-row {
            display: flex;
            flex-wrap: wrap;
            gap: 1rem;
        }

        .form-row .form-group {
            flex: 1;
            min-width: 0;
        }

        @media (max-width: 768px) {
            .form-row {
                flex-direction: column;
            }
        }
    </style>
</head>
<body>
<div class="auth-container">
    <div class="auth-card">
        <div class="auth-header">
            <div class="auth-icon">
                <i class="fas fa-music"></i>
            </div>
            <h2>Create Your RhythmWave Account</h2>
            <p class="text-secondary">Join our community of music lovers today</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                    ${error}
            </div>
        </c:if>

        <form id="signupForm" action="${pageContext.request.contextPath}/register" method="post" novalidate>
            <div class="form-group">
                <label class="form-label">Account Type</label>
                <div class="user-type-options">
                    <div class="user-type-option" id="listener-option">
                        <input type="radio" name="user-type" value="listener" id="listener" checked>
                        <label for="listener" style="cursor: pointer;">
                            <i class="fas fa-headphones fa-2x mb-2"></i>
                            <div>Music Listener</div>
                            <small class="text-secondary">Enjoy music</small>
                        </label>
                    </div>
                    <div class="user-type-option" id="artist-option">
                        <input type="radio" name="user-type" value="artist" id="artist">
                        <label for="artist" style="cursor: pointer;">
                            <i class="fas fa-guitar fa-2x mb-2"></i>
                            <div>Artist</div>
                            <small class="text-secondary">Share your music</small>
                        </label>
                    </div>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="first-name" class="form-label">First Name</label>
                    <input type="text" class="form-control" id="first-name" name="first-name"
                           placeholder="Enter your first name" required minlength="2" maxlength="50">
                    <div class="invalid-feedback" id="first-name-error"></div>
                </div>
                <div class="form-group">
                    <label for="last-name" class="form-label">Last Name</label>
                    <input type="text" class="form-control" id="last-name" name="last-name"
                           placeholder="Enter your last name" required minlength="2" maxlength="50">
                    <div class="invalid-feedback" id="last-name-error"></div>
                </div>
            </div>

            <div class="form-group">
                <label for="email" class="form-label">Email address</label>
                <input type="email" class="form-control" id="email" name="email" placeholder="name@example.com"
                       required>
                <div class="invalid-feedback" id="email-error"></div>
            </div>

            <div class="form-row">
                <div class="form-group password-input-group">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" id="password" name="password"
                           placeholder="Create a strong password" required minlength="4">
                    <div class="invalid-feedback" id="password-error"></div>
                </div>
                <div class="form-group password-input-group">
                    <label for="signup-confirm-password" class="form-label">Confirm Password</label>
                    <input type="password" class="form-control" id="signup-confirm-password"
                           placeholder="Confirm your password" required minlength="4">
                    <div class="invalid-feedback" id="confirm-password-error"></div>
                </div>
            </div>

            <input type="hidden" id="userType" name="userType" value="listener">

            <button type="submit" class="btn btn-primary btn-lg" id="submit-btn">Continue</button>

            <div class="text-center mt-4">
                <p class="text-secondary">Already have an account? <a href="login.jsp" class="text-primary">Log In</a></p>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/signup.js"></script>
</body>
</html>