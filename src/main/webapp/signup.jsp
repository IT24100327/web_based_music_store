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
  <style>
    :root {
      --dark-bg: #121212;
      --card-bg: #1e1e1e;
      --primary: #bb86fc;
      --secondary: #03dac6;
      --text-primary: #ffffff;
      --text-secondary: #b3b3b3;
      --placeholder-color: #888;
    }

    body {
      background-color: var(--dark-bg);
      color: var(--text-primary);
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      min-height: 100vh;
      display: flex;
      flex-direction: column;
      background-image: linear-gradient(to bottom, #1a1a1a, #2d2d2d);
    }

    .auth-container {
      display: flex;
      justify-content: center;
      align-items: center;
      flex-grow: 1;
      padding: 2rem;
    }

    .auth-card {
      background-color: var(--card-bg);
      border-radius: 12px;
      padding: 2.5rem;
      width: 100%;
      max-width: 500px;
      box-shadow: 0 15px 35px rgba(0,0,0,0.5);
    }

    .auth-header {
      text-align: center;
      margin-bottom: 2.5rem;
    }

    .auth-icon {
      font-size: 3rem;
      color: var(--primary);
      margin-bottom: 1.5rem;
    }

    .form-control {
      background-color: #2a2a2a;
      border: 1px solid #444;
      color: var(--text-primary);
      padding: 0.85rem 2.5rem 0.85rem 1.2rem;
      font-size: 1.05rem;
      margin-bottom: 1.5rem;
    }

    .form-control:focus {
      background-color: #2a2a2a;
      color: var(--text-primary);
      border-color: var(--primary);
      box-shadow: 0 0 0 0.3rem rgba(187, 134, 252, 0.25);
    }

    .form-control::placeholder {
      color: var(--placeholder-color);
      opacity: 1;
    }

    .form-control:-ms-input-placeholder {
      color: var(--placeholder-color);
    }

    .form-control::-ms-input-placeholder {
      color: var(--placeholder-color);
    }

    .btn-primary {
      background-color: var(--primary);
      border: none;
      color: #000;
      padding: 0.9rem;
      font-weight: 600;
      font-size: 1.1rem;
      border-radius: 8px;
      transition: all 0.3s;
      width: 100%;
      margin-top: 1rem;
    }

    .btn-primary:hover {
      background-color: #9965f4;
      transform: translateY(-2px);
      box-shadow: 0 5px 15px rgba(187, 134, 252, 0.4);
    }

    .form-check-input {
      background-color: #2a2a2a;
      border: 1px solid #444;
      width: 1.2em;
      height: 1.2em;
      margin-top: 0.15em;
    }

    .form-check-input:checked {
      background-color: var(--primary);
      border-color: var(--primary);
    }

    .form-check-label {
      margin-left: 0.5em;
    }

    .password-input-group {
      position: relative;
    }

    .password-toggle {
      cursor: pointer;
      position: absolute;
      right: 10px;
      top: 50%;
      transform: translateY(-50%);
      color: var(--text-secondary);
      font-size: 1.1rem;
      display: flex;
      align-items: center;
      justify-content: center;
      height: 100%;
      padding: 0 10px;
    }

    .footer {
      background-color: #1a1a1a;
      padding: 1.5rem 0;
      margin-top: auto;
      text-align: center;
    }

    .footer-links {
      margin-top: 0.5rem;
    }

    .footer-links a {
      color: var(--text-secondary);
      text-decoration: none;
      margin: 0 12px;
      font-size: 0.95rem;
      transition: color 0.3s;
    }

    .footer-links a:hover {
      color: var(--primary);
    }

    .form-label {
      font-weight: 500;
      margin-bottom: 0.5rem;
      display: block;
    }

    .terms-text {
      margin-left: 1.8rem;
      margin-top: 0.5rem;
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

    <form id="signupForm" action="${pageContext.request.contextPath}/RegisterServlet" method="post">
      <div class="form-group">
        <label for="first-name" class="form-label">First Name</label>
        <input type="text" class="form-control" id="first-name" name="first-name" placeholder="Enter your first name" required>
      </div>

      <div class="form-group">
        <label for="last-name" class="form-label">Last Name</label>
        <input type="text" class="form-control" id="last-name" name="last-name" placeholder="Enter your last name" required>
      </div>

      <div class="form-group">
        <label for="email" class="form-label">Email address</label>
        <input type="email" class="form-control" id="email" name="email" placeholder="name@example.com" required>
      </div>

      <div class="form-group password-input-group">
        <label for="password" class="form-label">Password</label>
        <input type="password" class="form-control" id="password" name="password" placeholder="Create a strong password" required>
        <span class="password-toggle" id="signup-password-toggle">
          <i class="far fa-eye"></i>
        </span>
      </div>

      <div class="form-group password-input-group">
        <label for="signup-confirm-password" class="form-label">Confirm Password</label>
        <input type="password" class="form-control" id="signup-confirm-password" placeholder="Confirm your password" required>
        <span class="password-toggle" id="signup-confirm-password-toggle">
          <i class="far fa-eye"></i>
        </span>
      </div>

      <div class="form-group">
        <label for="signup-genre" class="form-label">Favorite Music Genre</label>
        <select class="form-control" id="signup-genre" name="signup-genre">
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
      </div>

<%--      <div class="form-group">--%>
<%--        <div class="form-check">--%>
<%--          <input type="checkbox" class="form-check-input" id="terms-agree" required>--%>
<%--          <label class="form-check-label" for="terms-agree">I agree to the Terms of Service and Privacy Policy</label>--%>
<%--        </div>--%>
<%--        <div class="terms-text">--%>
<%--          <a href="#" class="text-primary">Terms of Service</a>  <a href="#" class="text-primary">Privacy Policy</a>--%>
<%--        </div>--%>
<%--      </div>--%>

      <button type="submit" class="btn btn-primary btn-lg">Create Account</button>

      <div class="text-center mt-4">
        <p class="text-secondary">Already have an account? <a href="login.jsp" class="text-primary">Log In</a></p>
      </div>
    </form>
  </div>
</div>

<!-- Footer -->
<footer class="footer">
  <div class="container">
    <p class="text-secondary mb-0">© 2023 RhythmWave. All rights reserved.</p>
    <div class="footer-links">
      <a href="#">Privacy Policy</a>
      <a href="#">Terms of Service</a>
      <a href="#">Support</a>
      <a href="#">About Us</a>
      <a href="#">Artists</a>
    </div>
  </div>
</footer>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
<%--<script>--%>
<%--  document.addEventListener('DOMContentLoaded', function() {--%>

<%--    // Form validation--%>
<%--    document.getElementById('signupForm').addEventListener('submit', function(e) {--%>
<%--      e.preventDefault();--%>
<%--      const firstName = document.getElementById('first-name').value;--%>
<%--      const lastName = document.getElementById('last-name').value;--%>
<%--      const email = document.getElementById('email').value;--%>
<%--      const password = document.getElementById('password').value;--%>
<%--      const confirmPassword = document.getElementById('signup-confirm-password').value;--%>

<%--      if (!firstName || !lastName || !email || !password || !confirmPassword) {--%>
<%--        alert('Please fill in all required fields');--%>
<%--        return;--%>
<%--      }--%>

<%--      if (password !== confirmPassword) {--%>
<%--        alert('Passwords do not match');--%>
<%--      }--%>

<%--    });--%>
<%--  });--%>
<%--</script>--%>
</body>
</html>