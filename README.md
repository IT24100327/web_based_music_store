# Web-Based Music Store

A comprehensive web-based music store application built with Java, Servlets, and JSP. This platform allows users to browse, search, and purchase music tracks, while providing artists with a space to manage their profiles and music. Administrators have a dedicated dashboard to oversee the entire platform.

## Features

### For Users:
- **User Authentication:** Secure sign-up and login functionality.
- **Browse and Search:** Easily search for music tracks and artists.
- **Shopping Cart:** Add and manage tracks in a shopping cart.
- **Checkout and Payment:** A seamless checkout process with payment integration.
- **Order History:** View past orders and their details.
- **Community Engagement:** Participate in the community through posts and feedback.
- **Support:** Create and manage support tickets.

### For Artists:
- **Artist Profiles:** Create and manage artist profiles.
- **Track Management:** Upload and manage music tracks.
- **Profile Updates:** Keep artist profiles updated with the latest information.

### For Administrators:
- **Admin Dashboard:** A centralized dashboard for managing the platform.
- **User Management:** Manage all registered users.
- **Artist Management:** Manage artist profiles and their tracks.
- **Track Management:** Oversee all music tracks on the platform.
- **Order Management:** View and manage all user orders.
- **Payment Management:** Monitor and manage payments.
- **Content Management:** Manage community posts and feedback.
- **Support Management:** Respond to and manage support tickets.

## Technologies Used

- **Backend:** Java, Jakarta Servlets
- **Frontend:** JSP (JavaServer Pages), JSTL (JSP Standard Tag Library), HTML, CSS, JavaScript
- **Database:** MySQL
- **Build Tool:** Apache Maven
- **Server:** Apache Tomcat (or any other servlet container)
- **Libraries:**
    - `jbcrypt` for password hashing
    - `gson` and `json` for JSON processing
    - `jave-core` for audio file handling
    - `jakarta.mail` for email functionalities

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 22 or later
- Apache Maven
- Apache Tomcat 10.0 or later
- MySQL Server

### Installation

1. **Clone the repository:**
   ```sh
   git clone <repository-url>
   cd web_based_music_store
   ```
2. **Build the project:**
   ```sh
   mvn clean install
   ```
   This will generate a `web_based_music_store.war` file in the `target` directory.

### Database Setup

1. **Create a MySQL database:**
   ```sql
   CREATE DATABASE music_db;
   ```
2. **Database Schema:**
   The database schema is not included in the repository. You will need to create the necessary tables for users, artists, tracks, orders, etc. based on the DAO and model classes.

3. **Configure the database connection:**
   Open `src/main/java/utils/DatabaseConnection.java` and update the database URL, username, and password:
   ```java
   private static String DB_URL = "jdbc:mysql://localhost:3306/music_db?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
   private static String DB_USER = "your_db_username";
   private static String DB_PASSWORD = "your_db_password";
   ```

### Running the Application

1. **Deploy the WAR file:**
   Copy the `target/web_based_music_store.war` file to the `webapps` directory of your Apache Tomcat installation.

2. **Start Tomcat:**
   Start the Apache Tomcat server. The application will be deployed and accessible at `http://localhost:8080/web_based_music_store`.

## Project Structure

```
.
├── pom.xml                 # Maven project configuration
├── src
│   ├── main
│   │   ├── java            # Java source code
│   │   │   ├── controller  # Servlets for handling requests
│   │   │   ├── dao         # Data Access Objects for database interaction
│   │   │   ├── model       # Java beans representing data models
│   │   │   ├── service     # Business logic
│   │   │   └── utils       # Utility classes
│   │   └── webapp          # Web application resources
│   │       ├── admin       # JSP files for the admin dashboard
│   │       ├── artist      # JSP files for artist profiles
│   │       ├── community   # JSP files for community features
│   │       ├── css         # CSS stylesheets
│   │       ├── js          # JavaScript files
│   │       ├── WEB-INF     # Web application configuration
│   │       └── *.jsp       # JSP files for the main application
│   └── test                # Test sources
└── target                  # Compiled code and packaged WAR file
```

## Contributing

Contributions are welcome! Please feel free to submit a pull request.