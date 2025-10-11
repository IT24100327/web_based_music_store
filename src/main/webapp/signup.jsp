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

    <form id="signupForm" action="${pageContext.request.contextPath}/register" method="post" novalidate>
      <div class="form-group">
        <label for="first-name" class="form-label">First Name</label>
        <input type="text" class="form-control" id="first-name" name="first-name" placeholder="Enter your first name" required minlength="2" maxlength="50">
        <div class="invalid-feedback" id="first-name-error"></div>
      </div>

      <div class="form-group">
        <label for="last-name" class="form-label">Last Name</label>
        <input type="text" class="form-control" id="last-name" name="last-name" placeholder="Enter your last name" required minlength="2" maxlength="50">
        <div class="invalid-feedback" id="last-name-error"></div>
      </div>

      <div class="form-group">
        <label for="email" class="form-label">Email address</label>
        <input type="email" class="form-control" id="email" name="email" placeholder="name@example.com" required>
        <div class="invalid-feedback" id="email-error"></div>
      </div>

      <div class="form-group password-input-group">
        <label for="password" class="form-label">Password</label>
        <input type="password" class="form-control" id="password" name="password" placeholder="Create a strong password" required minlength="8">
        <span class="password-toggle" id="signup-password-toggle">
          <i class="far fa-eye"></i>
        </span>
        <div class="invalid-feedback" id="password-error"></div>
      </div>

      <div class="form-group password-input-group">
        <label for="signup-confirm-password" class="form-label">Confirm Password</label>
        <input type="password" class="form-control" id="signup-confirm-password" placeholder="Confirm your password" required minlength="8">
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

      <button type="submit" class="btn btn-primary btn-lg" id="submit-btn">Create Account</button>

      <div class="text-center mt-4">
        <p class="text-secondary">Already have an account? <a href="login.jsp" class="text-primary">Log In</a></p>
      </div>
    </form>
  </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
<script>
  // Password toggle functionality
  function togglePasswordVisibility(inputId, toggleId) {
    const input = document.getElementById(inputId);
    const toggle = document.getElementById(toggleId);
    const icon = toggle.querySelector('i');
    toggle.addEventListener('click', function() {
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

  // Form validation
  document.getElementById('signupForm').addEventListener('submit', function(e) {
    let isValid = true;

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

    if (!isValid) {
      e.preventDefault();
      const firstError = document.querySelector('.is-invalid');
      if (firstError) firstError.scrollIntoView({ behavior: 'smooth' });
    }
  });

  document.getElementById('signup-confirm-password').addEventListener('input', function() {
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
    document.getElementById(id).addEventListener('blur', function() {
      const value = this.value.trim();
      const errorDiv = document.getElementById(id + '-error');
      this.classList.remove('is-invalid', 'is-valid');
      errorDiv.textContent = '';
      let isFieldValid = true;
      if (!value) {
        isFieldValid = false;
        errorDiv.textContent = this.name.replace('-', ' ') + ' is required.';
      } else if (id === 'first-name' || id === 'last-name') {
        if (!validateName(value)) {
          isFieldValid = false;
          errorDiv.textContent = this.name.replace('-', ' ') + ' must be 2-50 letters only.';
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