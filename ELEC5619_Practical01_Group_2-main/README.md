# Game Pre-Order & Release Tracker
## Project Overview
### Problem 
There are many games being released every day, and it is challenging for individuals to keep track of the release dates for all the games they are interested in. The information on the release dates of games may also be spread across multiple platforms, with no central source for all of them. This can lead to people missing the release dates for games they wanted to play. 

Our project plans to tackle this problem by creating a system that gathers information on upcoming titles and helps the users keep track of all the games they are interested in.

### Main Features
- List upcoming games alongside their release dates
- Display countdown timer to games releases
- Allow users to join a wishlist for games they are interested in
- Notify users when games that are on their wishlist are released
- Provide links alongside games to a service where users can pre-order the game

## Team Roles
All members are developers and will test their own code.
- Thomas: Product Owner -> Manages the product backlog, user stories and acceptance criteria. Prioritizes tasks for each sprint.
- Long: Scrum Master -> Upholds scrum principles. Manages scrum meetings e.g., Sprint Retrospectives.
- Craig: Database specialist -> Designs DB schema and ensures data integrity.
- Dickson: Frontend specialist (React) -> Defines the conventions for components, state management and styling. Helps maintain quality of UI/UX. 
- Iverson: Backend specialist -> Defines the structure for controllers, services, etc. Ensures correct implementation of business logic. 

## Database Setup and Connection (Docker)
This project uses Docker to run a PostgreSQL database and a pgAdmin management interface in isolated containers. This ensures a consistent development environment for everyone on the team.

**Prerequisites**
- Git installed on your machine.
- Docker Desktop installed and running.

**Step-by-Step Guide**
1. Clone the Repository

    Clone this repository to your local machine and navigate into the project directory.
    ```bash
    git clone <your-repository-url>
    cd <your-repository-directory>
    ```

2. Verify .env file

    The database and pgAdmin credentials are managed in the .env file, which should have the following inside:
    ```
    POSTGRES_USER=username
    POSTGRES_PASSWORD=password
    POSTGRES_DB=app_db
    PGADMIN_DEFAULT_EMAIL=email@email.com
    PGADMIN_DEFAULT_PASSWORD=password
    ```

3. Start the Database

    Open a terminal in the project root and run the following command:
    ```Bash
    docker-compose up -d
    ```
    This will download the necessary images and start the PostgreSQL and pgAdmin containers in the background.

4. Verify the Setup

   - **Check the containers**: Run `docker-compose ps`. You should see two services (`database` and `pgadmin`) with a status of "running" or "up".
   - **Connect with pgAdmin**:
     - Open your web browser and navigate to http://localhost:15433. 
     - Make sure you can log in using the `PGADMIN_DEFAULT_EMAIL` and `PGADMIN_DEFAULT_PASSWORD` from the `.env` file.

5. Register the Database Server
   - Once logged in, you need to tell pgAdmin how to find our PostgreSQL container. 
   - In the left-hand browser panel, right-click on Servers and select Register > Server....
   - A dialog box will appear. Fill in the following tabs:
     - In the General tab:
        - Name: Give your connection a descriptive name, like `local-game-tracker-db`.
     - In the Connection tab:
       - Host name/address: `database` (Note: Use the service name database from the compose.yaml file. Do not use localhost, as pgAdmin is running in a different container and needs to connect via the shared Docker network.)
       - Port: `5432`
       - Maintenance database: `app_db` (from .env file)
       - Username: `username` (from .env file)
       - Password: `password` (from .env file)

6. Save and Connect 
   - Click the Save button.
   - If the connection is successful, you will now see your server listed in the left-hand panel. You can expand it to navigate through Databases > app_db > Schemas > public > Tables to see the tables created by the Spring Boot application.

You can now right-click on any table to view its data or open the Query Tool to run custom SQL commands.

**Useful Docker Commands**
- Stop the containers: `docker-compose down`
    - View live logs: `docker-compose logs -f`
    - Stop and delete all data (for a full reset): `docker-compose down -v`

## Package Guide
This guide explains the role of each package in our Spring Boot project structure.

`config`

Contains configuration classes. Think of this as the setup and customization area for the application.
Classes here define "beans" (objects managed by Spring) and rules that apply globally.
For example, our DataSeeder class in this package runs on startup to populate the database
with initial data like user roles.

`model`

Contains the JPA entities, also known as the domain model. 
Each class here represents a table in the database (e.g., the Game class maps to the games table).
These classes define the application's core data structures and the relationships between them.

`repository`

Contains the Spring Data JPA repository interfaces - data access interfaces.
This is the layer that directly communicates with the database. 
By extending Spring's JpaRepository, we get a full set of standard CRUD (Create, Read, Update, Delete) 
operations for free, without writing any implementation code.
This provides an abstraction over the database, and allows us to query data with custom methods (with/without SQL).

`service` _(TBC - To Be Completed)_

Will contain the service classes, which hold the core business logic of the application.
The service layer acts as the brain of the application. 
It coordinates the operations, calling methods from one or more repositories to gather data, 
enforcing business rules and calculations, and then preparing the result.

`dto` _(TBC - To Be Completed)_

Will contain Data Transfer Objects. 
DTOs are simple classes that define the "shape" of data being sent to/from our API. 
They act as a protective layer, ensuring we never expose our internal database model entities directly.
This is crucial for security (e.g., hiding a user's password hash) and 
for creating stable APIs that don't break when we change our database structure.

`controller` _(TBC - To Be Completed)_

Will contain the API endpoints. Controllers are the entry point for all external requests.
Classes here use annotations like @RestController and @GetMapping to define API URLs (e.g., /api/games).
Their job is to receive an incoming web request, pass the request data to
the appropriate service method for processing, and then return the final result as a response to the client.

