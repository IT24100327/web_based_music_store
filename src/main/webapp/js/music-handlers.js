// REPLACE the entire contents of this file with the following:

// Global audio player variables
let globalAudioPlayer = null;
let currentPlayingButton = null;

// Finds the audio player and initializes play button listeners
function initializePlayButtons() {
    globalAudioPlayer = document.getElementById('global-audio-player');
    if (!globalAudioPlayer) {
        console.error('Global audio player not found!');
        return;
    }

    // Reset listener to avoid duplicates
    document.removeEventListener('click', handlePlayButtonClick);
    document.addEventListener('click', handlePlayButtonClick);

    globalAudioPlayer.addEventListener('ended', () => {
        if (currentPlayingButton) {
            const icon = currentPlayingButton.querySelector('i');
            icon.classList.remove('fa-pause');
            icon.classList.add('fa-play');
            currentPlayingButton = null;
        }
    });
}

// Handles clicks on any play button
function handlePlayButtonClick(e) {
    const playButton = e.target.closest('.play-btn-sm');
    if (!playButton) {
        return;
    }
    e.preventDefault();

    const trackId = playButton.dataset.trackId;
    const icon = playButton.querySelector('i');

    // If clicking the currently playing track, pause it
    if (currentPlayingButton === playButton && !globalAudioPlayer.paused) {
        globalAudioPlayer.pause();
        icon.classList.remove('fa-pause');
        icon.classList.add('fa-play');
    } else {
        // If another track was playing, reset its button icon
        if (currentPlayingButton) {
            const prevIcon = currentPlayingButton.querySelector('i');
            prevIcon.classList.remove('fa-pause');
            prevIcon.classList.add('fa-play');
        }

        // Set the new track source and play it
        globalAudioPlayer.src = `${window.contextPath}/stream?trackId=${trackId}`;
        globalAudioPlayer.play();
        icon.classList.remove('fa-play');
        icon.classList.add('fa-pause');
        currentPlayingButton = playButton; // Set the current button
    }
}

// This function can be kept if you have view controls
function initializeViewControls() {
    document.querySelectorAll('.view-btn').forEach(btn => {
        btn.addEventListener('click', function () {
            const view = this.getAttribute('data-view');
            document.querySelectorAll('.view-btn').forEach(b => b.classList.remove('active'));
            this.classList.add('active');

            const grid = document.getElementById('track-row');
            if (grid) {
                if (view === 'list') {
                    grid.parentElement.classList.add('list-view');
                } else {
                    grid.parentElement.classList.remove('list-view');
                }
            }
        });
    });
}