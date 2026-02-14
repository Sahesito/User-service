## 👤 User Service – SmartCommerce
This module manages all user-related data and enforces role-based access control across the platform. It operates as an independent microservice registered in Eureka and secured using JWT authentication.

##🎯 Why This Module Exists
- User Lifecycle Management
  Handles creation, updates, soft deletion, permanent deletion, and reactivation of users.

- Role-Based Access Control (RBAC)
  Enforces authorization rules using roles (ADMIN, SELLER, CLIENT) with method-level security.

- JWT-Based Security
  Validates tokens, extracts user identity and role, and establishes authentication in a stateless manner.

- Soft Delete Strategy
  Deactivates users instead of permanently removing them by default, preserving historical integrity.

- Data Validation & Error Handling
  Uses request validation and a global exception handler to ensure consistent API responses.

- Service-to-Service Communication
  Exposes a dedicated /users/from-auth endpoint to allow the Auth Service to create users safely.

- Database Isolation
  Uses its own PostgreSQL database (smartcommerce_users) to maintain true microservice independence.

## 🔐 Security Design
- Stateless session management
- JWT filter executed before authentication processing
- Protected endpoints require ADMIN or SELLER roles depending on operation
- Public endpoints:

  **/actuator/** **
  
  **/users/from-auth**

## 🌐 Core Endpoints
| Endpoint                           | Access                  |
| ---------------------------------- | ----------------------- |
| `GET /users`                       | ADMIN, SELLER           |
| `GET /users/{id}`                  | ADMIN, SELLER           |
| `GET /users/email/{email}`         | ADMIN, SELLER           |
| `GET /users/role/{role}`           | ADMIN                   |
| `GET /users/active/{active}`       | ADMIN                   |
| `POST /users`                      | ADMIN                   |
| `PUT /users/{id}`                  | ADMIN                   |
| `DELETE /users/{id}` (soft delete) | ADMIN                   |
| `DELETE /users/{id}/permanent`     | ADMIN                   |
| `PATCH /users/{id}/reactivate`     | ADMIN                   |
| `POST /users/from-auth`            | Internal (Auth Service) |

## 🗄️ Persistence
- Database: PostgreSQL
- JPA with Hibernate (ddl-auto: update)
- Automatic timestamps (createdAt, updatedAt)
- Unique email constraint
- Initial seed data includes predefined ADMIN, SELLER, and CLIENT accounts.

## ⚙️ Configuration
- Port: 8082
- Registered in Eureka
- JWT secret configured via application.yml
- Actuator endpoints enabled (health, info)
- SQL initialization enabled
