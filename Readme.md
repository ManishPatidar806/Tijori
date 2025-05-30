# Tijori

Tijori is a cross-platform expense tracker application featuring a Flutter-based frontend and a Java Spring Boot backend. The project is organized into two main components:

- **Frontend**: A Flutter app supporting Android, iOS, Web, Windows, macOS, and Linux.
- **Backend**: A Spring Boot application providing RESTful APIs for expense management.

---

## System Architecture

The following diagram illustrates the high-level architecture of Tijori:

![System Architecture](Resources/ArchitectureDiagram.png)

- **Frontend**: Communicates with the backend via REST APIs.
- **Backend**: Handles business logic, data storage, and API endpoints.
- **Database**: Stores user and expense data (configured in backend).

---

## Sequence Diagram

Below is a typical sequence of operations for adding a new expense:

![Sequence Diagram](Resources/SequenceDiagram.png)

1. User interacts with the Flutter app UI.
2. The app sends a POST request to the backend API.
3. The backend processes the request and stores the expense in the database.
4. The backend responds with a success or error message.
5. The app updates the UI accordingly.

---

## Project Structure

```
Tijori/
│
├── Backend/      # Java Spring Boot backend
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   └── ...
│
└── Frontend/     # Flutter frontend
    ├── lib/
    ├── pubspec.yaml
    ├── android/
    ├── ios/
    ├── web/
    ├── windows/
    ├── macos/
    ├── linux/
    └── ...
```

---

## Features

- Track and manage expenses.
- Data visualization with charts (`fl_chart`).
- Modern UI with Lottie animations.
- Preferences and settings with `shared_preferences`.
- REST API integration with the backend.

---

## Getting Started

### Prerequisites

- [Flutter SDK](https://flutter.dev/docs/get-started/install)
- [Java 21+](https://www.oracle.com/java/technologies/downloads/#java21) (for backend)
- [Maven](https://maven.apache.org/) (for backend)
- [Docker](https://www.docker.com/) (optional, for containerized backend)

### Backend Setup

1. Navigate to the `Backend` directory:
    ```sh
    cd Backend
    ```
2. Copy `.env.example` to `.env` and configure environment variables as needed.
3. Build and run the backend:
    ```sh
    ./mvnw spring-boot:run
    ```
   Or with Docker:
    ```sh
    docker build -t tijori-backend .
    docker run --env-file .env -p 8080:8080 tijori-backend
    ```

### Frontend Setup

1. Navigate to the `Frontend` directory:
    ```sh
    cd Frontend
    ```
2. Install dependencies:
    ```sh
    flutter pub get
    ```
3. Run the app on your desired platform:
    ```sh
    flutter run
    ```

---

## Configuration

- **Backend**: Configure database and API settings in `Backend/.env`.
- **Frontend**: Update API endpoints in the Flutter code as needed.

---

## Dependencies

### Frontend

- `flutter_secure_storage`
- `shared_preferences`
- `http`
- `intl`
- `lottie`
- `fl_chart`
- `cupertino_icons`
- `another_telephony`
- `permission_handler`

See [`Frontend/pubspec.yaml`](Frontend/pubspec.yaml) for the full list.

### Backend

- Java 21+
- Spring Boot
- Maven

See [`Backend/pom.xml`](Backend/pom.xml) for details.

---

## Screenshots

![App Screenshot](Frontend/Screenshot_20250530_161220.png)

---

## License

This project is for educational purposes. See individual files for license information.

---

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

---

## Contact

For questions or support, please open an issue in this repository.