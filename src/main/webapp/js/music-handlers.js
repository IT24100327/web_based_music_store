// Music Handlers: Play and View
function initializeViewControls() {
    document.querySelectorAll('.view-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            const view = this.getAttribute('data-view');
            document.querySelectorAll('.view-btn').forEach(b => b.classList.remove('active'));
            this.classList.add('active');

            const grid = document.getElementById('track-row');
            if (view === 'list') {
                grid.parentElement.classList.add('list-view');
            } else {
                grid.parentElement.classList.remove('list-view');
            }
        });
    });
}

function initializePlayButtons() {
    document.addEventListener('click', function(e) {
        if (e.target.matches('.play-btn-sm, .play-btn-sm i')) {
            e.preventDefault();
            togglePlayButton(e.target.closest('.play-btn-sm'));
        }
    });
}

function togglePlayButton(button) {
    button.classList.toggle('playing');
    const icon = button.querySelector('i');
    if (icon.classList.contains('fa-play')) {
        icon.classList.replace('fa-play', 'fa-pause');
        console.log('Play started');
    } else {
        icon.classList.replace('fa-pause', 'fa-play');
        console.log('Play paused');
    }
}