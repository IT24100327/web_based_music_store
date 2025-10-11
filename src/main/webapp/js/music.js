document.addEventListener('DOMContentLoaded', function() {

    document.addEventListener('click', function(e) {
        if (e.target.matches('#pagination .page-link[data-page]')) {
            e.preventDefault();
            const link = e.target;
            const page = parseInt(link.getAttribute('data-page'));
            console.log('Pagination clicked for page:', page);

            const currentPageElem = document.querySelector('.page-item.active .page-link');
            const currentPage = parseInt(currentPageElem?.getAttribute('data-page') || '1');
            const noOfPages = window.noOfPages || 1;
            if (window.noOfPages === undefined) {
                console.warn('noOfPages not set from server; using fallback 1');
            }
            console.log('Current page:', currentPage, 'Total pages:', noOfPages);  // Debug

            if (page < 1 || page > noOfPages || link.parentElement.classList.contains('disabled')) {
                console.log('Invalid page, skipping');  // Debug
                return;
            }

            // Show loading state
            const trackRow = document.getElementById('track-row');
            if (trackRow) {
                trackRow.innerHTML = '<div class="col-12 text-center"><i class="fas fa-spinner fa-spin"></i> Loading...</div>';
            }

            // AJAX fetch with headers
            fetch(`${window.contextPath}/trackPaginate?page=${page}&ajax=true`, {
                method: 'GET',
                headers: {
                    'X-Requested-With': 'XMLHttpRequest',
                    'Accept': 'text/html'
                }
            })
                .then(response => {
                    console.log('Fetch response status:', response.status);  // Debug
                    if (!response.ok) throw new Error(`Network response was not ok: ${response.status}`);
                    return response.text();
                })
                .then(html => {
                    console.log('Received HTML length:', html.length);  // Debug: Should be >1000
                    // Safer replacement using DOMParser
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
                    }

                    // Re-init cart for new buttons (play is delegated, so no need)
                    if (typeof window.reInitCart === 'function') {
                        window.reInitCart();
                    }

                    // Smooth scroll to tracks
                    const trackSection = document.querySelector('.track-section');
                    if (trackSection) {
                        const navbar = document.querySelector('.navbar');
                        const navbarHeight = navbar ? navbar.offsetHeight : 0;
                        const elementTop = trackSection.getBoundingClientRect().top + window.scrollY;
                        const offsetTop = elementTop - navbarHeight;
                        window.scrollTo({
                            top: offsetTop,
                            behavior: 'smooth'
                        });
                    }

                    console.log('Update complete');  // Debug
                })
                .catch(error => {
                    console.error('Error loading tracks:', error);
                    if (trackRow) {
                        trackRow.innerHTML = '<div class="col-12 text-center text-danger">Error loading tracks. Please try again.</div>';
                    }
                });
        }
    });

    // Event delegation for play buttons (works on new ones too)
    document.addEventListener('click', function(e) {
        if (e.target.matches('.play-btn i')) {  // Target the <i> inside .play-btn
            e.preventDefault();  // Optional, since no href
            const button = e.target.closest('.play-btn');
            button.classList.toggle('playing');
            const icon = button.querySelector('i');
            if (icon.classList.contains('fa-play')) {
                icon.classList.replace('fa-play', 'fa-pause');
            } else {
                icon.classList.replace('fa-pause', 'fa-play');
            }
            console.log('Play toggled');  // Debug
        }
    });

    // Initial cart init (once)
    if (typeof window.reInitCart === 'function') {
        window.reInitCart();
    }

});