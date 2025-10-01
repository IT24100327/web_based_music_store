document.addEventListener('DOMContentLoaded', function() {
    const playButtons = document.querySelectorAll('.play-btn');
    playButtons.forEach(button => {
        button.addEventListener('click', function() {
            this.classList.toggle('playing');
            const icon = this.querySelector('i');
            if (icon.classList.contains('fa-play')) {
                icon.classList.replace('fa-play', 'fa-pause');
            } else {
                icon.classList.replace('fa-pause', 'fa-play');
            }
        });
    });
});