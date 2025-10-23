<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>RhythmWave - Artist Profile</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
  <style>
    .genre-checkboxes {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
      gap: 0.5rem;
      margin-top: 0.5rem;
    }

    .genre-checkbox {
      display: flex;
      align-items: center;
      gap: 0.5rem;
    }

    .invalid-feedback {
      display: block;
      color: #dc3545;
      font-size: 0.875em;
      margin-top: 0.25rem;
    }

    .is-invalid {
      border-color: #dc3545;
    }

    .is-valid {
      border-color: #198754;
    }

    /* Horizontal form layout */
    .form-row {
      display: flex;
      flex-wrap: wrap;
      gap: 1rem;
    }

    .form-row .form-group {
      flex: 1;
      min-width: 0;
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

    @media (max-width: 768px) {
      .form-row {
        flex-direction: column;
      }
    }
  </style>
</head>
<body>
<div class="auth-container">
  <div class="auth-card">
    <div class="auth-header">
      <div class="auth-icon">
        <i class="fas fa-guitar"></i>
      </div>
      <h2>Complete Your Artist Profile</h2>
      <p class="text-secondary">Tell us about your musical journey</p>
    </div>

    <form id="artistForm" action="${pageContext.request.contextPath}/register" method="post" novalidate>
      <input type="hidden" name="first-name" value="${param['first-name']}">
      <input type="hidden" name="last-name" value="${param['last-name']}">
      <input type="hidden" name="email" value="${param.email}">
      <input type="hidden" name="password" value="${param.password}">
      <input type="hidden" name="user-type" value="${param.userType}">

      <div class="form-row">
        <div class="form-group">
          <label for="stage-name" class="form-label">Stage Name *</label>
          <input type="text" class="form-control" id="stage-name" name="stage-name"
                 placeholder="Enter your stage name" required maxlength="50">
          <div class="invalid-feedback" id="stage-name-error"></div>
        </div>
      </div>

      <div class="form-row">
        <div class="form-group">
          <label for="bio" class="form-label">Artist Bio *</label>
          <textarea class="form-control" id="bio" name="bio"
                    placeholder="Tell us about yourself as an artist..."
                    rows="4" required maxlength="500"></textarea>
          <div class="invalid-feedback" id="bio-error"></div>
          <small class="text-secondary"><span id="bio-counter">0</span>/500 characters</small>
        </div>
      </div>

      <div class="form-group">
        <label class="form-label">Specialized Genres *</label>
        <div class="genre-checkboxes">
          <div class="genre-checkbox">
            <input type="checkbox" name="specialized-genres" value="Rock" id="genre-rock">
            <label for="genre-rock">Rock</label>
          </div>
          <div class="genre-checkbox">
            <input type="checkbox" name="specialized-genres" value="Pop" id="genre-pop">
            <label for="genre-pop">Pop</label>
          </div>
          <div class="genre-checkbox">
            <input type="checkbox" name="specialized-genres" value="Jazz" id="genre-jazz">
            <label for="genre-jazz">Jazz</label>
          </div>
          <div class="genre-checkbox">
            <input type="checkbox" name="specialized-genres" value="Hip Hop" id="genre-hiphop">
            <label for="genre-hiphop">Hip Hop</label>
          </div>
          <div class="genre-checkbox">
            <input type="checkbox" name="specialized-genres" value="Electronic" id="genre-electronic">
            <label for="genre-electronic">Electronic</label>
          </div>
          <div class="genre-checkbox">
            <input type="checkbox" name="specialized-genres" value="Classical" id="genre-classical">
            <label for="genre-classical">Classical</label>
          </div>
          <div class="genre-checkbox">
            <input type="checkbox" name="specialized-genres" value="Country" id="genre-country">
            <label for="genre-country">Country</label>
          </div>
          <div class="genre-checkbox">
            <input type="checkbox" name="specialized-genres" value="R&B" id="genre-rnb">
            <label for="genre-rnb">R&B</label>
          </div>
        </div>
        <div class="invalid-feedback" id="specialized-genres-error"></div>
        <small class="text-secondary">Select at least one genre you specialize in</small>
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

<script src="${pageContext.request.contextPath}/js/signup-artist.js"></script>
</body>
</html>