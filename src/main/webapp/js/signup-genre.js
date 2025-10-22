// Genre selection functionality
function initializeGenreSelection() {
    const genreCards = document.querySelectorAll('.genre-card');
    const selectedGenreInput = document.getElementById('selectedGenre');
    const genreError = document.getElementById('genre-error');
    const form = document.getElementById('genreForm');

    if (!form) return;

    // Initialize genre card selection
    function initializeGenreCards() {
        genreCards.forEach(card => {
            card.addEventListener('click', function () {
                // Remove selected class from all cards
                genreCards.forEach(c => c.classList.remove('selected'));

                // Add selected class to clicked card
                this.classList.add('selected');

                // Set the selected genre value
                selectedGenreInput.value = this.getAttribute('data-genre');

                // Clear error
                genreError.style.display = 'none';
            });
        });
    }

    // Form validation
    function initializeFormValidation() {
        form.addEventListener('submit', function (e) {
            if (!selectedGenreInput.value) {
                e.preventDefault();
                genreError.style.display = 'block';
            }
        });
    }

    // Initialize all functionality
    initializeGenreCards();
    initializeFormValidation();
}

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', initializeGenreSelection);