# Products Service

## Setting Up the Local Environment

### Option 1: Easiest Way - Using Docker Compose

#### Prerequisites:

Before running the **Products Service** with Docker Compose, ensure you have the following:

- **Docker** installed on your machine.
- The following ports are not in use by other applications:
    - **8080** for the application
    - **6379** for Redis
    - **5432** for Postgres

#### Steps to Get the Products Service Running with Docker Compose:

1. Clone the project repository.
2. Navigate to the project root directory.
3. Run the following command:

   ```bash
   docker compose up

This will start the `postgres`, `redis`, and `app` services defined in the `docker-compose.yml` file.

**Note:** The application uses **Liquibase** to automatically create the necessary tables in the `productsDB` database upon startup.

### Option 2: Debugging With IntelliJ Ultimate Edition

If you have **IntelliJ Ultimate Edition**, you can debug the **Products Service** while running **Redis** and **Postgres** through Docker.

#### Steps:

**1. Set up Docker Compose as a run configuration in IntelliJ:**

- Open your project in **IntelliJ**.
- Navigate to **Run > Edit Configurations**.
- Click the **➕** icon and select **Docker-Compose**.
- Select the `docker-compose.yml` file in the project directory.
- Apply the changes and save.

**2. Run the application:**

- Start the **Docker Compose** configuration in IntelliJ.
- The application will run with **Redis** and **Postgres** containers.

### Option 3: Debugging Without IntelliJ Ultimate Edition

If you don’t have **IntelliJ Ultimate**, you can still debug the **Products Service** by using **Docker Compose** for **Redis** and **Postgres** while running the app manually.

#### Steps:

**1. Start Redis and Postgres using Docker Compose:**

Run the following command to start only the **Postgres** and **Redis** services:

    ```bash
    docker compose up postgres redis

This will start the **Postgres** and **Redis** containers, skipping the **app** service.

**2. Manually run the application in IntelliJ:**

- Open **IntelliJ**.  
- Run the **Products Service** manually in **debug mode** using IntelliJ's default build configuration.

### Option 4: Running Without Docker
If you don't want to use Docker, you can run the services locally and connect the application manually.

#### Steps:

1. Install **PostgreSQL** and **Redis** on your machine.
2. Create a **PostgreSQL** database called `productsDB` and a user with:
    - **Username:** `root`
    - **Password:** `root` (or update these values in the application properties file if needed).
3. Ensure **Redis** is running locally on **port 6379**.
4. Run the **Products Service** using the **default build configuration**.  

### Swagger UI and API Endpoints

Once the **Products Service** is running, you can access the **Swagger UI** at:  
🔗 [Swagger UI](http://localhost:8080/swagger-ui/index.html)

#### API Endpoints:

| Endpoint                | Method | Description               |
|-------------------------|--------|---------------------------|
| `/api/v1/products`      | POST   | Create a new product      |
| `/api/v1/products`      | GET    | Get all products          |
| `/api/v1/products/{id}` | GET    | Get a product by ID       |

