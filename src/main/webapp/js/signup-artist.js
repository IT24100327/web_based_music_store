// Artist form validation and functionality
function initializeArtistForm() {
    const bioTextarea = document.getElementById('bio');
    const bioCounter = document.getElementById('bio-counter');
    const stageNameInput = document.getElementById('stage-name');
    const genreCheckboxes = document.querySelectorAll('input[name="specialized-genres"]');
    const form = document.getElementById('artistForm');

    if (!form) return;

    // Bio character counter
    function initializeBioCounter() {
        if (bioTextarea && bioCounter) {
            bioTextarea.addEventListener('input', function () {
                const length = this.value.length;
                bioCounter.textContent = length;

                if (length > 500) {
                    this.classList.add('is-invalid');
                    document.getElementById('bio-error').textContent = 'Bio cannot exceed 500 characters.';
                } else {
                    this.classList.remove('is-invalid');
                    document.getElementById('bio-error').textContent = '';
                    if (length > 0) {
                        this.classList.add('is-valid');
                    }
                }
            });
        }
    }

    // Stage name validation
    function initializeStageNameValidation() {
        if (stageNameInput) {
            stageNameInput.addEventListener('input', function () {
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
        }
    }

    // Genre validation
    function initializeGenreValidation() {
        if (genreCheckboxes.length > 0) {
            genreCheckboxes.forEach(checkbox => {
                checkbox.addEventListener('change', validateGenres);
            });
        }
    }

    function validateGenres() {
        const checkedGenres = document.querySelectorAll('input[name="specialized-genres"]:checked');
        const errorDiv = document.getElementById('specialized-genres-error');

        if (checkedGenres.length === 0) {
            errorDiv.textContent = 'Please select at least one specialized genre.';
        } else {
            errorDiv.textContent = '';
        }
    }

    // Form validation
    function initializeFormValidation() {
        form.addEventListener('submit', function (e) {
            let isValid = true;

            // Stage name validation
            if (!stageNameInput.value.trim()) {
                stageNameInput.classList.add('is-invalid');
                document.getElementById('stage-name-error').textContent = 'Stage name is required.';
                isValid = false;
            }

            // Bio validation
            if (!bioTextarea.value.trim()) {
                bioTextarea.classList.add('is-invalid');
                document.getElementById('bio-error').textContent = 'Artist bio is required.';
                isValid = false;
            } else if (bioTextarea.value.length > 500) {
                bioTextarea.classList.add('is-invalid');
                document.getElementById('bio-error').textContent = 'Bio cannot exceed 500 characters.';
                isValid = false;
            }

            // Genre validation
            const checkedGenres = document.querySelectorAll('input[name="specialized-genres"]:checked');
            if (checkedGenres.length === 0) {
                document.getElementById('specialized-genres-error').textContent = 'Please select at least one specialized genre.';
                isValid = false;
            }

            if (!isValid) {
                e.preventDefault();
                const firstError = document.querySelector('.is-invalid');
                if (firstError) firstError.scrollIntoView({behavior: 'smooth'});
            }
        });
    }

    // Initialize all functionality
    initializeBioCounter();
    initializeStageNameValidation();
    initializeGenreValidation();
    initializeFormValidation();
}

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', initializeArtistForm);