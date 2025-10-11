// Music Pagination and Track Content
function initializePagination() {
    document.addEventListener('click', function(e) {
        if (e.target.matches('#pagination .page-link[data-page]')) {
            e.preventDefault();
            handlePaginationClick(e.target);
        }
    });
}

function handlePaginationClick(link) {
    const page = parseInt(link.getAttribute('data-page'));
    console.log('Pagination clicked for page:', page);

    const currentPageElem = document.querySelector('.page-item.active .page-link');
    const currentPage = parseInt(currentPageElem?.getAttribute('data-page') || '1');
    const noOfPages = window.noOfPages || 1;

    console.log('Current page:', currentPage, 'Total pages:', noOfPages);

    if (page < 1 || page > noOfPages || link.parentElement.classList.contains('disabled')) {
        console.log('Invalid page, skipping');
        return;
    }

    loadTracksPage(page);
}

function loadTracksPage(page) {
    const trackRow = document.getElementById('track-row');
    if (trackRow) {
        trackRow.innerHTML = '<div class="col-12 text-center"><i class="fas fa-spinner fa-spin"></i> Loading tracks...</div>';
    }

    fetch(`${window.contextPath}/trackPaginate?page=${page}&ajax=true`, {
        method: 'GET',
        headers: {
            'X-Requested-With': 'XMLHttpRequest',
            'Accept': 'text/html'
        }
    })
        .then(response => {
            console.log('Fetch response status:', response.status);
            if (!response.ok) {
                throw new Error(`Network response was not ok: ${response.status}`);
            }
            return response.text();
        })
        .then(html => {
            console.log('Received HTML length:', html.length);
            updateTrackContent(html);
            scrollToTracks();

            if (typeof window.reInitCart === 'function') {
                window.reInitCart();
            }

            console.log('Page update complete');
        })
        .catch(error => {
            console.error('Error loading tracks:', error);
            handleTracksLoadError(error);
        });
}

function updateTrackContent(html) {
    const parser = new DOMParser();
    const doc = parser.parseFromString(html, 'text/html');
    const newRow = doc.getElementById('track-row');
    const newNav = doc.querySelector('nav.mt-5');

    if (newRow) {
        const oldRow = document.getElementById('track-row');
        if (oldRow) oldRow.innerHTML = newRow.innerHTML;
    }

    if (newNav) {
        const oldNav = document.querySelector('nav.mt-5');
        if (oldNav) oldNav.innerHTML = newNav.innerHTML;
    } else {
        const oldNav = document.querySelector('nav.mt-5');
        if (oldNav) oldNav.remove();
    }
}

function scrollToTracks() {
    const trackSection = document.querySelector('.main-content');
    if (trackSection) {
        const navbar = document.querySelector('.navbar');
        const navbarHeight = navbar ? navbar.offsetHeight : 0;
        const elementTop = trackSection.getBoundingClientRect().top + window.scrollY;
        const offsetTop = elementTop - navbarHeight - 20;

        window.scrollTo({
            top: offsetTop,
            behavior: 'smooth'
        });
    }
}

function handleTracksLoadError(error) {
    const trackRow = document.getElementById('track-row');
    if (trackRow) {
        trackRow.innerHTML = `
            <div class="col-12 text-center text-danger">
                <i class="fas fa-exclamation-triangle"></i>
                <p>Error loading tracks. Please try again.</p>
                <button class="btn btn-primary mt-2" onclick="location.reload()">Retry</button>
            </div>
        `;
    }
}