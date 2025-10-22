<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RhythmWave - Select Your Genre</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
    <style>
        .genre-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
            gap: 0.75rem;
            margin-top: 1rem;
        }

        .genre-card {
            background-color: #2a2a2a;
            border: 2px solid #444;
            border-radius: 12px;
            padding: 1rem;
            text-align: center;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .genre-card:hover {
            border-color: #bb86fc;
            transform: translateY(-5px);
        }

        .genre-card.selected {
            border-color: #bb86fc;
            background-color: rgba(187, 134, 252, 0.1);
        }

        .genre-icon {
            font-size: 1.5rem;
            margin-bottom: 0.5rem;
            color: #bb86fc;
        }

        .invalid-feedback {
            display: block;
            color: #dc3545;
            font-size: 0.875em;
            margin-top: 0.25rem;
        }

        /* Back button styling */
        .btn-back {
            background-color: transparent;
            border: 2px solid var(--primary);
            color: var(--primary);
            padding: 0.9rem;
            font-weight: 600;
            font-size: 1.1rem;
            border-radius: 8px;
            transition: all 0.3s ease;
            animation: fadeInUp 0.7s ease-out;
            animation-fill-mode: backwards;
            animation-delay: 0.7s;
        }

        .btn-back:hover {
            background-color: var(--primary);
            color: #000;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(187, 134, 252, 0.4);
        }
    </style>
</head>
<body>
<div class="auth-container">
    <div class="auth-card">
        <div class="auth-header">
            <div class="auth-icon">
                <i class="fas fa-headphones"></i>
            </div>
            <h2>Select Your Favorite Genre</h2>
            <p class="text-secondary">Choose the music genres you love most</p>
        </div>

        <form id="genreForm" action="${pageContext.request.contextPath}/register" method="post">
            <input type="hidden" name="first-name" value="${param['first-name']}">
            <input type="hidden" name="last-name" value="${param['last-name']}">
            <input type="hidden" name="email" value="${param.email}">
            <input type="hidden" name="password" value="${param.password}">
            <input type="hidden" name="userType" value="${param.userType}">

            <div class="form-group">
                <label class="form-label">Favorite Music Genre</label>
                <div class="genre-grid">
                    <div class="genre-card" data-genre="rock">
                        <div class="genre-icon">
                            <i class="fas fa-guitar"></i>
                        </div>
                        <div>Rock</div>
                    </div>
                    <div class="genre-card" data-genre="pop">
                        <div class="genre-icon">
                            <i class="fas fa-microphone"></i>
                        </div>
                        <div>Pop</div>
                    </div>
                    <div class="genre-card" data-genre="jazz">
                        <div class="genre-icon">
                            <i class="fas fa-music"></i>
                        </div>
                        <div>Jazz</div>
                    </div>
                    <div class="genre-card" data-genre="hiphop">
                        <div class="genre-icon">
                            <i class="fas fa-drum"></i>
                        </div>
                        <div>Hip Hop</div>
                    </div>
                    <div class="genre-card" data-genre="electronic">
                        <div class="genre-icon">
                            <i class="fas fa-sliders-h"></i>
                        </div>
                        <div>Electronic</div>
                    </div>
                    <div class="genre-card" data-genre="classical">
                        <div class="genre-icon">
                            <i class="fas fa-violin"></i>
                        </div>
                        <div>Classical</div>
                    </div>
                    <div class="genre-card" data-genre="country">
                        <div class="genre-icon">
                            <i class="fas fa-hat-cowboy"></i>
                        </div>
                        <div>Country</div>
                    </div>
                    <div class="genre-card" data-genre="r&b">
                        <div class="genre-icon">
                            <i class="fas fa-soul"></i>
                        </div>
                        <div>R&B</div>
                    </div>
                </div>
                <input type="hidden" id="selectedGenre" name="favorite-genre" required>
                <div class="invalid-feedback" id="genre-error">Please select your favorite genre.</div>
            </div>

            <div class="d-flex justify-content-between mt-4">
                <button type="button" class="btn btn-back me-2" onclick="window.history.back()">
                    <i class="fas fa-arrow-left"></i> Back
                </button>
                <button type="submit" class="btn btn-primary">Complete Registration</button>
            </div>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/signup-genre.js"></script>
</body>
</html>