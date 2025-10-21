<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RhythmWave - Sign Up</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/signup.css">
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

        .artist-fields {
            background-color: rgba(187, 134, 252, 0.05);
            padding: 1.5rem;
            border-radius: 8px;
            border: 1px solid #333;
            margin-top: 1rem;
        }

        .genre-checkboxes {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
            gap: 0.5rem;
            margin-top: 0.5rem;
        }

        .genre-checkbox {
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }
    </style>
</head>
<body>
<!-- Auth Container -->
<div class="auth-container">
    <div class="auth-card">
        <div class="auth-header">
            <div class="auth-icon">
                <i class="fas fa-music"></i>
            </div>
            <h2>Create Your RhythmWave Account</h2>
            <p class="text-secondary">Join our community of music lovers today</p>
        </div>

        <!-- Display error messages -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                    ${error}
            </div>
        </c:if>

        <form id="signupForm" action="${pageContext.request.contextPath}/register" method="post" novalidate>
            <!-- User Type Selection -->
            <div class="form-group">
                <label class="form-label">Account Type</label>
                <div class="user-type-options">
                    <div class="user-type-option" id="listener-option">
                        <input type="radio" name="user-type" value="listener" id="listener" checked>
                        <label for="listener" style="cursor: pointer;">
                            <i class="fas fa-headphones fa-2x mb-2"></i>
                            <div>Music Listener</div>
                            <small class="text-muted">Enjoy music</small>
                        </label>
                    </div>
                    <div class="user-type-option" id="artist-option">
                        <input type="radio" name="user-type" value="artist" id="artist">
                        <label for="artist" style="cursor: pointer;">
                            <i class="fas fa-guitar fa-2x mb-2"></i>
                            <div>Artist</div>
                            <small class="text-muted">Share your music</small>
                        </label>
                    </div>
                </div>
            </div>

            <!-- Basic Information -->
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

            <div class="form-group">
                <label for="email" class="form-label">Email address</label>
                <input type="email" class="form-control" id="email" name="email" placeholder="name@example.com"
                       required>
                <div class="invalid-feedback" id="email-error"></div>
            </div>

            <div class="form-group password-input-group">
                <label for="password" class="form-label">Password</label>
                <input type="password" class="form-control" id="password" name="password"
                       placeholder="Create a strong password" required minlength="4">
                <span class="password-toggle" id="signup-password-toggle">
          <i class="far fa-eye"></i>
        </span>
                <div class="invalid-feedback" id="password-error"></div>
            </div>

            <div class="form-group password-input-group">
                <label for="signup-confirm-password" class="form-label">Confirm Password</label>
                <input type="password" class="form-control" id="signup-confirm-password"
                       placeholder="Confirm your password" required minlength="4">
                <span class="password-toggle" id="signup-confirm-password-toggle">
          <i class="far fa-eye"></i>
        </span>
                <div class="invalid-feedback" id="confirm-password-error"></div>
            </div>

            <div class="form-group">
                <label for="signup-genre" class="form-label">Favorite Music Genre</label>
                <select class="form-control" id="signup-genre" name="signup-genre" required>
                    <option value="">Select your favorite genre</option>
                    <option value="rock">Rock</option>
                    <option value="pop">Pop</option>
                    <option value="jazz">Jazz</option>
                    <option value="hiphop">Hip Hop</option>
                    <option value="electronic">Electronic</option>
                    <option value="classical">Classical</option>
                    <option value="country">Country</option>
                    <option value="r&b">R&B</option>
                </select>
                <div class="invalid-feedback" id="genre-error"></div>
            </div>

            <div class="artist-fields" id="artist-fields" style="display: none;">
                <h5 class="mb-3"><i class="fas fa-guitar"></i> Artist Information</h5>
                <div class="form-group">
                    <label for="stage-name" class="form-label">Stage Name</label>
                    <input type="text" class="form-control" id="stage-name" name="stage-name"
                           placeholder="Enter your stage name" maxlength="50">
                    <div class="invalid-feedback" id="stage-name-error"></div>
                </div>
                <div class="form-group">
                    <label for="bio" class="form-label">Artist Bio</label>
                    <textarea class="form-control" id="bio" name="bio"
                              placeholder="Tell us about yourself as an artist..." rows="4" maxlength="500"></textarea>
                    <div class="invalid-feedback" id="bio-error"></div>
                    <small class="text-muted">Brief description of your musical background and style (max 500
                        characters)</small>
                </div>
                <div class="form-group">
                    <label class="form-label">Specialized Genres</label>
                    <div class="genre-checkboxes">
                        <div class="genre-checkbox">
                            <input type="checkbox" name="specialized-genres" value="Rock" id="genre-rock">
                            <label for="genre-rock">Rock</label>
                        </div>
                        <div class="genre-checkbox">
                            <input type="checkbox" name="specialized-genres" value="Pop" id="genre-pop">
                            <label for="genre-pop">Pop</label>
                        </div>
                        <div class="genre-checkbox">
                            <input type="checkbox" name="specialized-genres" value="Jazz" id="genre-jazz">
                            <label for="genre-jazz">Jazz</label>
                        </div>
                        <div class="genre-checkbox">
                            <input type="checkbox" name="specialized-genres" value="Hip Hop" id="genre-hiphop">
                            <label for="genre-hiphop">Hip Hop</label>
                        </div>
                        <div class="genre-checkbox">
                            <input type="checkbox" name="specialized-genres" value="Electronic" id="genre-electronic">
                            <label for="genre-electronic">Electronic</label>
                        </div>
                        <div class="genre-checkbox">
                            <input type="checkbox" name="specialized-genres" value="Classical" id="genre-classical">
                            <label for="genre-classical">Classical</label>
                        </div>
                        <div class="genre-checkbox">
                            <input type="checkbox" name="specialized-genres" value="Country" id="genre-country">
                            <label for="genre-country">Country</label>
                        </div>
                        <div class="genre-checkbox">
                            <input type="checkbox" name="specialized-genres" value="R&B" id="genre-rnb">
                            <label for="genre-rnb">R&B</label>
                        </div>
                    </div>
                    <div class="invalid-feedback" id="specialized-genres-error"></div>
                    <small class="text-muted">Select the genres you specialize in as an artist</small>
                </div>
            </div>

            <button type="submit" class="btn btn-primary btn-lg" id="submit-btn">Create Account</button>

            <div class="text-center mt-4">
                <p class="text-secondary">Already have an account? <a href="login.jsp" class="text-primary">Log In</a>
                </p>
            </div>
        </form>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // User type selection
    document.addEventListener('DOMContentLoaded', function () {
        const listenerOption = document.getElementById('listener-option');
        const artistOption = document.getElementById('artist-option');
        const artistFields = document.getElementById('artist-fields');
        const listenerRadio = document.getElementById('listener');
        const artistRadio = document.getElementById('artist');

        function updateUserTypeSelection() {
            if (artistRadio.checked) {
                artistOption.classList.add('selected');
                listenerOption.classList.remove('selected');
                artistFields.style.display = 'block';

                // Make artist fields required
                document.getElementById('bio').required = true;
                document.querySelectorAll('input[name="specialized-genres"]').forEach(cb => {
                    cb.required = true;
                });
            } else {
                artistOption.classList.remove('selected');
                listenerOption.classList.add('selected');
                artistFields.style.display = 'none';

                // Remove required from artist fields
                document.getElementById('bio').required = false;
                document.querySelectorAll('input[name="specialized-genres"]').forEach(cb => {
                    cb.required = false;
                });
            }
        }


        listenerOption.addEventListener('click', function () {
            listenerRadio.checked = true;
            updateUserTypeSelection();
        });

        artistOption.addEventListener('click', function () {
            artistRadio.checked = true;
            updateUserTypeSelection();
        });

        // Initialize selection
        updateUserTypeSelection();
    });

    // Password toggle functionality
    function togglePasswordVisibility(inputId, toggleId) {
        const input = document.getElementById(inputId);
        const toggle = document.getElementById(toggleId);
        const icon = toggle.querySelector('i');
        toggle.addEventListener('click', function () {
            const type = input.getAttribute('type') === 'password' ? 'text' : 'password';
            input.setAttribute('type', type);
            icon.classList.toggle('fa-eye');
            icon.classList.toggle('fa-eye-slash');
        });
    }

    togglePasswordVisibility('password', 'signup-password-toggle');
    togglePasswordVisibility('signup-confirm-password', 'signup-confirm-password-toggle');

    // Name validation (letters only, min 2 chars)
    function validateName(name) {
        const nameRegex = /^[a-zA-Z\s]{2,50}$/;
        return nameRegex.test(name);
    }

    // Email validation
    function validateEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

    // Bio validation
    function validateBio(bio) {
        return bio.length <= 500;
    }

    // Specialized genres validation
    function validateSpecializedGenres() {
        const checkboxes = document.querySelectorAll('input[name="specialized-genres"]:checked');
        return checkboxes.length > 0;
    }

    // Form validation
    document.getElementById('signupForm').addEventListener('submit', function (e) {
        let isValid = true;
        const isArtist = document.getElementById('artist').checked;

        // Reset previous states
        document.querySelectorAll('.form-control').forEach(input => {
            input.classList.remove('is-invalid', 'is-valid');
        });
        document.querySelectorAll('.invalid-feedback').forEach(div => {
            div.textContent = '';
        });

        // First Name
        const firstName = document.getElementById('first-name').value.trim();
        if (!firstName) {
            document.getElementById('first-name').classList.add('is-invalid');
            document.getElementById('first-name-error').textContent = 'First name is required.';
            isValid = false;
        } else if (!validateName(firstName)) {
            document.getElementById('first-name').classList.add('is-invalid');
            document.getElementById('first-name-error').textContent = 'First name must be 2-50 letters only.';
            isValid = false;
        } else {
            document.getElementById('first-name').classList.add('is-valid');
        }

        // Last Name
        const lastName = document.getElementById('last-name').value.trim();
        if (!lastName) {
            document.getElementById('last-name').classList.add('is-invalid');
            document.getElementById('last-name-error').textContent = 'Last name is required.';
            isValid = false;
        } else if (!validateName(lastName)) {
            document.getElementById('last-name').classList.add('is-invalid');
            document.getElementById('last-name-error').textContent = 'Last name must be 2-50 letters only.';
            isValid = false;
        } else {
            document.getElementById('last-name').classList.add('is-valid');
        }

        // Email
        const email = document.getElementById('email').value.trim();
        if (!email) {
            document.getElementById('email').classList.add('is-invalid');
            document.getElementById('email-error').textContent = 'Email is required.';
            isValid = false;
        } else if (!validateEmail(email)) {
            document.getElementById('email').classList.add('is-invalid');
            document.getElementById('email-error').textContent = 'Please enter a valid email address.';
            isValid = false;
        } else {
            document.getElementById('email').classList.add('is-valid');
        }

        const password = document.getElementById('password').value;
        if (!password) {
            document.getElementById('password').classList.add('is-invalid');
            document.getElementById('password-error').textContent = 'Password is required.';
            isValid = false;
        } else if (password.length < 4) {
            document.getElementById('password').classList.add('is-invalid');
            document.getElementById('password-error').textContent = 'Password must be at least 4 characters.';
            isValid = false;
        } else {
            document.getElementById('password').classList.add('is-valid');
        }

        const confirmPassword = document.getElementById('signup-confirm-password').value;
        if (!confirmPassword) {
            document.getElementById('signup-confirm-password').classList.add('is-invalid');
            document.getElementById('confirm-password-error').textContent = 'Please confirm your password.';
            isValid = false;
        } else if (password !== confirmPassword) {
            document.getElementById('signup-confirm-password').classList.add('is-invalid');
            document.getElementById('confirm-password-error').textContent = 'Passwords do not match.';
            isValid = false;
        } else {
            document.getElementById('signup-confirm-password').classList.add('is-valid');
        }

        const genre = document.getElementById('signup-genre').value;
        if (!genre) {
            document.getElementById('signup-genre').classList.add('is-invalid');
            document.getElementById('genre-error').textContent = 'Please select your favorite genre.';
            isValid = false;
        } else {
            document.getElementById('signup-genre').classList.add('is-valid');
        }

        // Artist-specific validation
        if (isArtist) {
            const stageName = document.getElementById('stage-name').value.trim();
            if (!stageName) {
                document.getElementById('stage-name').classList.add('is-invalid');
                document.getElementById('stage-name-error').textContent = 'Stage name is required.';
                isValid = false;
            } else if (stageName.length > 50) {
                document.getElementById('stage-name').classList.add('is-invalid');
                document.getElementById('stage-name-error').textContent = 'Stage name cannot exceed 50 characters.';
                isValid = false;
            } else {
                document.getElementById('stage-name').classList.add('is-valid');
            }

            const bio = document.getElementById('bio').value.trim();
            if (!bio) {
                document.getElementById('bio').classList.add('is-invalid');
                document.getElementById('bio-error').textContent = 'Artist bio is required.';
                isValid = false;
            } else if (!validateBio(bio)) {
                document.getElementById('bio').classList.add('is-invalid');
                document.getElementById('bio-error').textContent = 'Bio cannot exceed 500 characters.';
                isValid = false;
            } else {
                document.getElementById('bio').classList.add('is-valid');
            }

            const yearsActive = document.getElementById('years-active').value;
            if (yearsActive && yearsActive < 0) {
                document.getElementById('years-active').classList.add('is-invalid');
                document.getElementById('years-active-error').textContent = 'Years active cannot be negative.';
                isValid = false;
            } else if (yearsActive) {
                document.getElementById('years-active').classList.add('is-valid');
            }

            const websiteUrl = document.getElementById('website-url').value.trim();
            if (websiteUrl && !validateUrl(websiteUrl)) {
                document.getElementById('website-url').classList.add('is-invalid');
                document.getElementById('website-url-error').textContent = 'Please enter a valid URL.';
                isValid = false;
            } else if (websiteUrl) {
                document.getElementById('website-url').classList.add('is-valid');
            }

            if (!validateSpecializedGenres()) {
                document.getElementById('specialized-genres-error').textContent = 'Please select at least one specialized genre.';
                isValid = false;
            }
        }

        if (!isValid) {
            e.preventDefault();
            const firstError = document.querySelector('.is-invalid');
            if (firstError) firstError.scrollIntoView({behavior: 'smooth'});
        }
    });

    // Real-time validation for bio
    // Real-time validation for new fields
    document.getElementById('stage-name').addEventListener('input', function () {
        const stageName = this.value.trim();
        const errorDiv = document.getElementById('stage-name-error');
        if (!stageName) {
            this.classList.add('is-invalid');
            errorDiv.textContent = 'Stage name is required.';
        } else if (stageName.length > 50) {
            this.classList.add('is-invalid');
            errorDiv.textContent = 'Stage name cannot exceed 50 characters.';
        } else {
            this.classList.remove('is-invalid');
            errorDiv.textContent = '';
            this.classList.add('is-valid');
        }
    });

    document.getElementById('years-active').addEventListener('input', function () {
        const yearsActive = this.value;
        const errorDiv = document.getElementById('years-active-error');
        if (yearsActive && yearsActive < 0) {
            this.classList.add('is-invalid');
            errorDiv.textContent = 'Years active cannot be negative.';
        } else {
            this.classList.remove('is-invalid');
            errorDiv.textContent = '';
            if (yearsActive) this.classList.add('is-valid');
        }
    });

    document.getElementById('website-url').addEventListener('input', function () {
        const websiteUrl = this.value.trim();
        const errorDiv = document.getElementById('website-url-error');
        if (websiteUrl && !validateUrl(websiteUrl)) {
            this.classList.add('is-invalid');
            errorDiv.textContent = 'Please enter a valid URL.';
        } else {
            this.classList.remove('is-invalid');
            errorDiv.textContent = '';
            if (websiteUrl) this.classList.add('is-valid');
        }
    });

    document.getElementById('bio').addEventListener('input', function () {
        const bio = this.value;
        const errorDiv = document.getElementById('bio-error');

        if (bio.length > 500) {
            this.classList.add('is-invalid');
            errorDiv.textContent = 'Bio cannot exceed 500 characters.';
        } else {
            this.classList.remove('is-invalid');
            errorDiv.textContent = '';
            if (bio.length > 0) {
                this.classList.add('is-valid');
            }
        }
    });

    // Real-time validation for specialized genres
    document.querySelectorAll('input[name="specialized-genres"]').forEach(checkbox => {
        checkbox.addEventListener('change', function () {
            const errorDiv = document.getElementById('specialized-genres-error');
            if (validateSpecializedGenres()) {
                errorDiv.textContent = '';
            }
        });
    });

    // Existing validation for other fields...
    document.getElementById('signup-confirm-password').addEventListener('input', function () {
        const password = document.getElementById('password').value;
        const confirmPassword = this.value;
        const confirmInput = document.getElementById('signup-confirm-password');
        const errorDiv = document.getElementById('confirm-password-error');
        if (confirmPassword && password !== confirmPassword) {
            confirmInput.classList.add('is-invalid');
            errorDiv.textContent = 'Passwords do not match.';
        } else if (confirmPassword) {
            confirmInput.classList.remove('is-invalid');
            confirmInput.classList.add('is-valid');
            errorDiv.textContent = '';
        }
    });

    ['first-name', 'last-name', 'email', 'signup-genre'].forEach(id => {
        document.getElementById(id).addEventListener('blur', function () {
            const value = this.value.trim();
            const errorDiv = document.getElementById(id + '-error');
            this.classList.remove('is-invalid', 'is-valid');
            errorDiv.textContent = '';
            let isFieldValid = true;
            if (!value) {
                isFieldValid = false;
                errorDiv.textContent = this.getAttribute('name').replace('-', ' ') + ' is required.';
            } else if (id === 'first-name' || id === 'last-name') {
                if (!validateName(value)) {
                    isFieldValid = false;
                    errorDiv.textContent = this.getAttribute('name').replace('-', ' ') + ' must be 2-50 letters only.';
                }
            } else if (id === 'email') {
                if (!validateEmail(value)) {
                    isFieldValid = false;
                    errorDiv.textContent = 'Please enter a valid email address.';
                }
            } else if (id === 'signup-genre') {
                if (!value) {
                    isFieldValid = false;
                    errorDiv.textContent = 'Please select your favorite genre.';
                }
            }
            if (isFieldValid) {
                this.classList.add('is-valid');
            } else {
                this.classList.add('is-invalid');
            }
        });
    });
</script>
</body>
</html>