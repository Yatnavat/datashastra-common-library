Generic Entity Creation Prompt Template
Project Context
I am working on [SERVICE_NAME] for [INDUSTRY/DOMAIN].
Technical Stack
Java Version: [VERSION - e.g., 17, 21]
Spring Boot Version: [VERSION - e.g., 3.2.0, 3.1.5]
Database: [TYPE & VERSION - e.g., PostgreSQL 15, MySQL 8.0, MongoDB 6.0]
Key Dependencies: [LIST - e.g., Spring Data JPA, Spring Security, Spring Validation]
Architecture: [PATTERN - e.g., Microservices, Monolith, Hexagonal]
Multi-Tenant SaaS Architecture
Tenant Isolation: [STRATEGY - e.g., Shared DB with tenant_id, Separate schemas, Separate databases]
Data Segregation: [METHOD - e.g., Row-level security, Schema-level, Database-level]
Client Onboarding: [APPROACH - e.g., Database-driven config, Code deployment, Hybrid]
Scaling: [STRATEGY - e.g., Horizontal with partitioning, Vertical, Auto-scaling]
Global Usage Requirements
Internationalization: [REQUIREMENTS - e.g., Multi-language, Multi-currency, Multi-timezone]
Regional Compliance: [REGULATIONS - e.g., GDPR, CCPA, HIPAA, SOX, Industry-specific]
Performance: [TARGETS - e.g., Sub-200ms response, 99.9% uptime, 1000 TPS]
Localization: [SPECIFICS - e.g., Regional business rules, Date/number formats, Currency handling]
Business Domain
Primary Entities: [MAIN_BUSINESS_OBJECTS - e.g., User, Product, Order, Customer]
Key Relationships: [ENTITY_CONNECTIONS - e.g., One-to-many, Many-to-many, Hierarchical]
Business Rules: [CRITICAL_CONSTRAINTS - e.g., Workflow states, Validation rules, Business logic]
Data Sensitivity: [CLASSIFICATION - e.g., PII, Financial, Public, Internal]
Task Description
Help with entity creation and review for [MODULE/FEATURE].

I have [STATUS - e.g., "already created X entities", "need to create new entities", "need to modify existing entities"] and need to [ACTION - e.g., "review each systematically", "design from scratch", "refactor current design"] for:

Data model accuracy and business logic alignment
Multi-tenant considerations and data isolation
Performance optimization (indexing, relationships, caching)
Validation rules and constraint definitions
Global usage compatibility (i18n, compliance, scalability)
Response Guidelines
No code generation unless explicitly requested
Focus on conceptual design and architectural decisions
Highlight potential issues with current approach
Suggest improvements for scalability and maintainability
Consider edge cases for global deployment
Recommend best practices for chosen tech stack
Address security implications for multi-tenant architecture
Current Task
[SPECIFIC_TASK_DESCRIPTION - e.g., "Let's review the first entity", "Design the User entity", "Analyze entity relationships"]


Quick Fill Examples by Industry:
E-Commerce Platform
Industry/Domain: Online retail and marketplace
Primary Entities: Customer, Product, Order, Cart, Payment, Inventory
Key Business Rules: Stock management, Pricing strategies, Order workflows
Financial Services
Industry/Domain: Banking and financial technology
Primary Entities: Account, Transaction, Customer, Card, Loan, Investment
Key Business Rules: Regulatory compliance, Risk management, Audit trails
Healthcare Management
Industry/Domain: Healthcare and patient management
Primary Entities: Patient, Provider, Appointment, Medical Record, Insurance
Key Business Rules: HIPAA compliance, Patient privacy, Clinical workflows
Transportation/Logistics
Industry/Domain: Transport and logistics management
Primary Entities: Vehicle, Route, Driver, Shipment, Customer, Location
Key Business Rules: Route optimization, Regulatory compliance, Tracking
Educational Technology
Industry/Domain: Learning management and education
Primary Entities: Student, Course, Instructor, Assignment, Grade, Institution
Key Business Rules: Academic policies, Progress tracking, Assessment rules


Usage Instructions:
Copy the template above
Replace all bracketed placeholders with your specific requirements
Fill in the Quick Fill section that matches your industry (or create custom)
Customize business domain section with your specific entities and rules
Adjust technical stack to match your project setup
Specify your current task at the bottom

This template ensures consistent, comprehensive entity design discussions across all your projects while maintaining flexibility for different domains and technical requirements.

