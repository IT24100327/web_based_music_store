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

// User type selection
function initializeUserTypeSelection() {
    const listenerOption = document.getElementById('listener-option');
    const artistOption = document.getElementById('artist-option');
    const listenerRadio = document.getElementById('listener');
    const artistRadio = document.getElementById('artist');
    const userTypeField = document.getElementById('userType');

    function updateUserTypeSelection() {
        if (artistRadio.checked) {
            artistOption.classList.add('selected');
            listenerOption.classList.remove('selected');
            userTypeField.value = 'artist';
        } else {
            artistOption.classList.remove('selected');
            listenerOption.classList.add('selected');
            userTypeField.value = 'listener';
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
}

// Form validation
function initializeFormValidation() {
    const form = document.getElementById('signupForm');
    if (!form) return;

    form.addEventListener('submit', function (e) {
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

        if (isValid) {
            // Change form action based on user type
            const userType = document.getElementById('userType').value;
            const contextPath = window.location.pathname.split('/')[1] || '';
            const basePath = contextPath ? `/${contextPath}` : '';

            if (userType === 'listener') {
                this.action = `${basePath}/signup-genre`;
            } else {
                this.action = `${basePath}/signup-artist`;
            }
        } else {
            e.preventDefault();
            const firstError = document.querySelector('.is-invalid');
            if (firstError) firstError.scrollIntoView({behavior: 'smooth'});
        }
    });
}

// Real-time validation
function initializeRealTimeValidation() {
    ['first-name', 'last-name', 'email'].forEach(id => {
        const element = document.getElementById(id);
        if (element) {
            element.addEventListener('blur', function () {
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
                }
                if (isFieldValid) {
                    this.classList.add('is-valid');
                } else {
                    this.classList.add('is-invalid');
                }
            });
        }
    });

    const confirmPasswordInput = document.getElementById('signup-confirm-password');
    if (confirmPasswordInput) {
        confirmPasswordInput.addEventListener('input', function () {
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
    }
}

// Initialize all functionality when DOM is loaded
document.addEventListener('DOMContentLoaded', function () {
    initializeUserTypeSelection();
    initializeFormValidation();
    initializeRealTimeValidation();

    // Initialize password toggles
    togglePasswordVisibility('password', 'signup-password-toggle');
    togglePasswordVisibility('signup-confirm-password', 'signup-confirm-password-toggle');
});