<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RhythmWave - Login</title>
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
            border-radius: 10px;
            padding: 2.5rem;
            width: 100%;
            max-width: 450px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.5);
        }

        .auth-header {
            text-align: center;
            margin-bottom: 2rem;
        }

        .auth-icon {
            font-size: 2.5rem;
            color: var(--primary);
            margin-bottom: 1rem;
        }

        .form-control {
            background-color: #2a2a2a;
            border: 1px solid #444;
            color: var(--text-primary);
            padding: 0.75rem 1rem;
        }

        .form-control:focus {
            background-color: #2a2a2a;
            color: var(--text-primary);
            border-color: var(--primary);
            box-shadow: 0 0 0 0.25rem rgba(187, 134, 252, 0.25);
        }

        .btn-primary {
            background-color: var(--primary);
            border: none;
            color: #000;
            padding: 0.75rem;
            font-weight: 600;
        }

        .btn-primary:hover {
            background-color: #9965f4;
        }

        .form-check-input {
            background-color: #2a2a2a;
            border: 1px solid #444;
        }

        .form-check-input:checked {
            background-color: var(--primary);
            border-color: var(--primary);
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

        .password-toggle {
            cursor: pointer;
            position: absolute;
            right: 12px;
            top: 50%;
            transform: translateY(-50%);
            color: var(--text-secondary);
        }

        .password-input-group {
            position: relative;
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
            margin: 0 10px;
            font-size: 0.9rem;
        }

        .footer-links a:hover {
            color: var(--primary);
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
            <h2>Welcome Back</h2>
            <p class="text-secondary">Sign in to your RhythmWave account</p>
        </div>

        <form id="loginForm" action="${pageContext.request.contextPath}/LoginServlet" method="post">
            <div class="mb-3">
                <label for="login-email" class="form-label">Email address</label>
                <input type="email" class="form-control" id="login-email" name="email" placeholder="name@example.com" required>
            </div>

            <div class="mb-3 password-input-group">
                <label for="login-password" class="form-label">Password</label>
                <input type="password" class="form-control" id="login-password" name="password" placeholder="Enter your password" required>
            </div>

            <div class="mb-3 d-flex justify-content-between align-items-center">
                <div class="form-check">
                    <input type="checkbox" class="form-check-input" id="remember-me" name="remember-me" value="true">
                    <label class="form-check-label" for="remember-me">Remember me</label>
                </div>
                <a href="#" class="text-primary">Forgot password?</a>
            </div>

            <div class="d-grid gap-2">
                <button type="submit" class="btn btn-primary btn-lg">Login</button>
                <a href="signup.jsp" class="btn btn-outline-light">Don't have an account? Sign Up</a>
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
        </div>
    </div>
</footer>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Form validation
        document.getElementById('loginForm').addEventListener('submit', function(e) {
            const email = document.getElementById('login-email').value;
            const password = document.getElementById('login-password').value;

            if (!email || !password) {
                e.preventDefault()
                alert('Please fill in all fields');
            }

        });
    });
</script>
</body>
</html>