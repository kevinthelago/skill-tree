# Controller Architecture

This document describes the controller layer architecture and patterns used in the Skill Tree backend.

## Architectural Principles

**CRITICAL: Separation of Concerns**

Controllers **MUST ONLY** define REST endpoints and delegate all business logic to service layers:

```kotlin
// ✅ CORRECT - Controller only handles HTTP concerns
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/login")
    fun loginUser(
        @RequestBody request: LoginRequest,
        httpRequest: HttpServletRequest,
        httpResponse: HttpServletResponse,
        session: HttpSession
    ): ResponseEntity<LoginResponse> {
        val response = authService.loginUser(request, session, httpRequest, httpResponse)
        return ResponseEntity.ok(response)
    }
}

// ❌ INCORRECT - Controller contains business logic
@RestController
class BadController {
    @PostMapping("/login")
    fun loginUser(...): ResponseEntity<LoginResponse> {
        // DON'T DO THIS - no validation logic in controllers
        if (request.email.isEmpty()) throw ValidationException()

        // DON'T DO THIS - no database operations in controllers
        val user = userRepository.findByEmail(request.email)

        // DON'T DO THIS - no business logic in controllers
        val token = rememberMeService.generateToken(...)
        val cookie = Cookie("remember_me", token)  // Business logic belongs in service
        response.addCookie(cookie)

        return ResponseEntity.ok(response)
    }
}
```

### Service Layer Responsibilities

- Business logic and validation
- Database operations and transactions
- External service integrations
- HTTP response manipulation (cookies, headers)
- Error handling and exception mapping
- Logging and monitoring
- **DTO construction and data transformation**
- **Filtering, sorting, and pagination logic**
- **Data aggregation and mapping**

### Controller Responsibilities

Controllers should **ONLY**:
- Define REST endpoint definitions (`@GetMapping`, `@PostMapping`, etc.)
- Bind request/response parameters (`@RequestBody`, `@PathVariable`, etc.)
- Delegate to service layer
- Return `ResponseEntity` wrappers

### What Controllers MUST NOT Contain

**CRITICAL: Controllers must be thin HTTP adapters with zero business logic.**

Controllers **MUST NOT** contain:

❌ **Business Logic**
```kotlin
// DON'T DO THIS
@GetMapping("/skills")
fun getSkills(): ResponseEntity<*> {
    val skills = skillRepository.findAll()
    if (skills.isEmpty()) {  // Business logic
        return ResponseEntity.notFound().build()
    }
    return ResponseEntity.ok(skills)
}
```

❌ **Map or Object Definitions** (use strongly-typed DTOs)
```kotlin
// DON'T DO THIS
@GetMapping("/metrics")
fun getMetrics(): ResponseEntity<Map<String, Any>> {
    return ResponseEntity.ok(mapOf(
        "cpu" to cpuUsage,
        "memory" to memoryUsage,
        "uptime" to uptime
    ))
}

// DO THIS - Define DTO and construct in service
@GetMapping("/metrics")
fun getMetrics(): ResponseEntity<MetricsResponse> {
    return ResponseEntity.ok(metricsService.getMetrics())
}
```

❌ **Filtering Logic**
```kotlin
// DON'T DO THIS
@GetMapping("/skills")
fun getSkills(@RequestParam category: String?): ResponseEntity<*> {
    val skills = skillRepository.findAll()
    val filtered = if (category != null) {
        skills.filter { it.category == category }  // Filtering logic
    } else skills
    return ResponseEntity.ok(filtered)
}

// DO THIS
@GetMapping("/skills")
fun getSkills(@RequestParam category: String?): ResponseEntity<List<Skill>> {
    return ResponseEntity.ok(skillService.getSkills(category))
}
```

❌ **Sorting Logic**
```kotlin
// DON'T DO THIS
@GetMapping("/skills")
fun getSkills(@RequestParam sortBy: String?): ResponseEntity<*> {
    val skills = skillRepository.findAll()
    val sorted = when (sortBy) {
        "name" -> skills.sortedBy { it.name }  // Sorting logic
        "level" -> skills.sortedBy { it.level }
        else -> skills
    }
    return ResponseEntity.ok(sorted)
}

// DO THIS
@GetMapping("/skills")
fun getSkills(@RequestParam sortBy: String?): ResponseEntity<List<Skill>> {
    return ResponseEntity.ok(skillService.getSkills(sortBy))
}
```

❌ **DTO Construction**
```kotlin
// DON'T DO THIS
@GetMapping("/dashboard")
fun getDashboard(): ResponseEntity<DashboardResponse> {
    val skills = skillService.getAll()
    val users = userService.getAll()

    // DTO construction belongs in service layer
    val response = DashboardResponse(
        skills = skills,
        totalSkills = skills.size,
        users = users,
        totalUsers = users.size
    )
    return ResponseEntity.ok(response)
}

// DO THIS
@GetMapping("/dashboard")
fun getDashboard(): ResponseEntity<DashboardResponse> {
    return ResponseEntity.ok(dashboardService.getDashboard())
}
```

❌ **Data Transformation**
```kotlin
// DON'T DO THIS
@GetMapping("/skills")
fun getSkills(): ResponseEntity<*> {
    val skills = skillService.getAll()
    val transformed = skills.map { skill ->  // Transformation logic
        SkillDto(
            id = skill.id,
            name = skill.name,
            level = skill.level.toString()
        )
    }
    return ResponseEntity.ok(transformed)
}

// DO THIS
@GetMapping("/skills")
fun getSkills(): ResponseEntity<List<SkillDto>> {
    return ResponseEntity.ok(skillService.getSkillsAsDto())
}
```

**Summary: Controller Pattern**
```kotlin
// ✅ PERFECT CONTROLLER - Only HTTP concerns
@RestController
@RequestMapping("/api/skills")
class SkillController(
    private val skillService: SkillService
) {
    private val logger = LoggerFactory.getLogger(SkillController::class.java)

    @GetMapping
    fun getSkills(
        @RequestParam(required = false) category: String?,
        @RequestParam(required = false) sortBy: String?
    ): ResponseEntity<SkillsResponse> {
        logger.info("GET /api/skills - category: $category, sortBy: $sortBy")
        val response = skillService.getSkills(category, sortBy)
        logger.info("GET /api/skills - Completed successfully")
        return ResponseEntity.ok(response)
    }
}
```

**CRITICAL: DTO Separation**

Controllers **MUST NOT** define DTOs (Data Transfer Objects) directly within the controller file. All DTOs must be separated into the `dto/` directory:

```kotlin
// ✅ CORRECT - DTOs defined in separate files
// File: src/main/kotlin/com/skilltree/skilltree/dto/AuthDto.kt
package com.skilltree.skilltree.dto

data class LoginRequest(
    val email: String,
    val password: String,
    val rememberMe: Boolean = false
)

data class LoginResponse(
    val success: Boolean,
    val user: UserDto,
    val sessionId: String,
    val rememberMeToken: String?
)

// File: src/main/kotlin/com/skilltree/skilltree/controller/AuthController.kt
@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {
    @PostMapping("/login")
    fun loginUser(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val response = authService.loginUser(request)
        return ResponseEntity.ok(response)
    }
}

// ❌ INCORRECT - DTOs defined inside controller file
@RestController
class BadController {
    data class LoginRequest(val email: String, val password: String)  // DON'T DO THIS
    data class LoginResponse(val token: String)  // DON'T DO THIS

    @PostMapping("/login")
    fun loginUser(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        // ...
    }
}
```

**DTO Organization:**
- Skill DTOs: `src/main/kotlin/com/skilltree/skilltree/dto/skill/`
- User DTOs: `src/main/kotlin/com/skilltree/skilltree/dto/user/`
- Auth DTOs: `src/main/kotlin/com/skilltree/skilltree/dto/AuthDto.kt`
- Progress DTOs: `src/main/kotlin/com/skilltree/skilltree/dto/progress/`

**Rationale:**
- **Reusability**: DTOs can be shared across multiple controllers and services
- **Testability**: DTOs can be tested independently and reused in test fixtures
- **Documentation**: Centralized location makes API contracts easier to find and document
- **Type Safety**: Prevents accidental DTO duplication across controllers
- **Maintainability**: Changes to DTOs are made in one place

### Rationale

- **Testability**: Service logic can be unit tested without HTTP layer
- **Reusability**: Services can be called from multiple controllers or scheduled jobs
- **Maintainability**: Clear separation makes code easier to understand and modify
- **Single Responsibility**: Each layer has one clear purpose

## Response Types

Always use standardized response wrappers for consistent API responses:

```kotlin
ResponseEntity<SkillResponse>        // Success with data
ResponseEntity<DeleteResponse>       // Delete operations
ResponseEntity<MessageResponse>      // Operations returning messages
ResponseEntity<ErrorResponse>        // Error responses (handled by controller advice)
```

## Controller Advice

**CRITICAL: Exception Handling Pattern**

Every controller directory **MUST** have a corresponding controller advice class to handle exceptions. Controller advice files must be located in the **same directory** as the controllers they serve.

### Controller Advice Location Rules

```
controller/
├── auth/
│   ├── AuthController.kt
│   ├── UserController.kt
│   └── AuthControllerAdvice.kt           ✅ Handles exceptions for all auth controllers
├── skill/
│   ├── SkillController.kt
│   ├── SkillCategoryController.kt
│   └── SkillControllerAdvice.kt          ✅ Handles exceptions for all skill controllers
├── progress/
│   ├── ProgressController.kt
│   ├── AchievementController.kt
│   └── ProgressControllerAdvice.kt       ✅ Handles exceptions for all progress controllers
└── admin/
    ├── AdminController.kt
    └── AdminControllerAdvice.kt          ✅ Handles exceptions for admin controllers
```

### Controller Advice Pattern

```kotlin
// ✅ CORRECT - Controller advice in same directory as controllers
// File: src/main/kotlin/com/skilltree/skilltree/controller/skill/SkillControllerAdvice.kt
package com.skilltree.skilltree.controller.skill

import com.skilltree.skilltree.exception.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.slf4j.LoggerFactory

@ControllerAdvice(basePackages = ["com.skilltree.skilltree.controller.skill"])
class SkillControllerAdvice {
    private val logger = LoggerFactory.getLogger(SkillControllerAdvice::class.java)

    @ExceptionHandler(SkillNotFoundException::class)
    fun handleSkillNotFound(e: SkillNotFoundException): ResponseEntity<ErrorResponse> {
        logger.error("Skill not found: ${e.message}")
        return ResponseEntity.status(404).body(ErrorResponse(
            errorCode = "SKILL_NOT_FOUND",
            message = e.message ?: "Skill not found"
        ))
    }

    @ExceptionHandler(InvalidSkillDataException::class)
    fun handleInvalidSkillData(e: InvalidSkillDataException): ResponseEntity<ErrorResponse> {
        logger.error("Invalid skill data: ${e.message}")
        return ResponseEntity.status(400).body(ErrorResponse(
            errorCode = "INVALID_SKILL_DATA",
            message = e.message ?: "Invalid skill data"
        ))
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(e: Exception): ResponseEntity<ErrorResponse> {
        logger.error("Unhandled exception in skill controller", e)
        return ResponseEntity.status(500).body(ErrorResponse(
            errorCode = "INTERNAL_ERROR",
            message = "An unexpected error occurred"
        ))
    }
}

// ❌ INCORRECT - Controller advice in separate 'advice/' directory
// File: src/main/kotlin/com/skilltree/skilltree/advice/SkillControllerAdvice.kt
// This violates the co-location principle
```

### When to Create Controller Advice

- **One advice per domain** - Group related controllers under one advice file
- **Separate advice for different concerns** - Different domains may need different error handling
- **Always co-locate** - Advice file must be in the same directory as its controllers
- **Handle domain-specific exceptions** - Each advice should handle exceptions specific to its domain

### Controllers Without Advice

Controllers should **NEVER** contain try-catch blocks. All exception handling must be delegated to controller advice:

```kotlin
// ❌ INCORRECT - Exception handling in controller
@PostMapping
fun createSkill(@RequestBody request: CreateSkillRequest): ResponseEntity<*> {
    try {
        val result = skillService.createSkill(request)
        return ResponseEntity.ok(result)
    } catch (e: InvalidSkillDataException) {
        return ResponseEntity.status(400).body(ErrorResponse("Invalid data"))
    } catch (e: Exception) {
        return ResponseEntity.status(500).body(ErrorResponse("Creation failed"))
    }
}

// ✅ CORRECT - Let controller advice handle exceptions
@PostMapping
fun createSkill(@RequestBody request: CreateSkillRequest): ResponseEntity<SkillResponse> {
    val result = skillService.createSkill(request)  // Exceptions handled by SkillControllerAdvice
    return ResponseEntity.ok(result)
}
```

## Controller Organization

Controllers are organized by domain:

- **auth/** - Authentication and user management
  - `AuthController` - Login, logout, session management
  - `UserController` - User CRUD operations

- **skill/** - Skill management
  - `SkillController` - Skill CRUD operations
  - `SkillCategoryController` - Category management

- **progress/** - Progress tracking
  - `ProgressController` - User progress tracking
  - `AchievementController` - Achievement management

- **admin/** - Administrative endpoints
  - `AdminController` - Administrative operations

## Logging Standards

Controllers should include structured logging:

```kotlin
@RestController
@RequestMapping("/api/skills")
class SkillController(
    private val skillService: SkillService
) {
    private val logger = LoggerFactory.getLogger(SkillController::class.java)

    @GetMapping("/{id}")
    fun getSkill(@PathVariable id: Long): ResponseEntity<SkillResponse> {
        logger.info("GET /api/skills/$id - Request received")
        val response = skillService.getSkill(id)
        logger.info("GET /api/skills/$id - Completed successfully")
        return ResponseEntity.ok(response)
    }

    @PostMapping
    fun createSkill(@RequestBody request: CreateSkillRequest): ResponseEntity<SkillResponse> {
        logger.info("POST /api/skills - Creating skill: ${request.name}")
        val response = skillService.createSkill(request)
        logger.info("POST /api/skills - Skill created with ID: ${response.id}")
        return ResponseEntity.ok(response)
    }
}
```

**Logging Guidelines:**
- Log entry and exit points for requests
- Log important business events (creation, deletion, etc.)
- Use appropriate log levels (INFO, WARN, ERROR)
- Include relevant context (IDs, names, etc.)
- Do NOT log sensitive data (passwords, tokens, etc.)

## Validation

Use Spring Boot validation annotations on DTOs, not in controllers:

```kotlin
// ✅ CORRECT - Validation in DTO
data class CreateSkillRequest(
    @field:NotBlank(message = "Skill name is required")
    @field:Size(min = 3, max = 100, message = "Skill name must be between 3 and 100 characters")
    val name: String,

    @field:Min(value = 1, message = "Level must be at least 1")
    @field:Max(value = 100, message = "Level must not exceed 100")
    val level: Int
)

// Controller remains clean
@PostMapping
fun createSkill(@Valid @RequestBody request: CreateSkillRequest): ResponseEntity<SkillResponse> {
    return ResponseEntity.ok(skillService.createSkill(request))
}

// ❌ INCORRECT - Validation logic in controller
@PostMapping
fun createSkill(@RequestBody request: CreateSkillRequest): ResponseEntity<*> {
    if (request.name.isBlank()) {
        return ResponseEntity.badRequest().body("Name is required")
    }
    if (request.level < 1 || request.level > 100) {
        return ResponseEntity.badRequest().body("Invalid level")
    }
    return ResponseEntity.ok(skillService.createSkill(request))
}
```

## Testing Controllers

Controllers should be tested using `@WebMvcTest` with mocked services:

```kotlin
@WebMvcTest(SkillController::class)
class SkillControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var skillService: SkillService

    @Test
    fun `should return skill by id`() {
        val skill = SkillResponse(id = 1L, name = "Kotlin", level = 5)
        `when`(skillService.getSkill(1L)).thenReturn(skill)

        mockMvc.perform(get("/api/skills/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Kotlin"))
    }
}
```

**Testing Guidelines:**
- Mock all service dependencies
- Test HTTP status codes and response bodies
- Test validation and error scenarios
- Do NOT test business logic (that's in service tests)
- Focus on controller-specific behavior (routing, binding, etc.)