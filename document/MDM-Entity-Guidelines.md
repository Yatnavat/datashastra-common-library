# MDM Entity Modeling Guidelines

## Core Principles

1. **Single Source of Truth**: Each master data entity should be the authoritative source for its domain
2. **Data Quality Focus**: Entities should enforce data quality through validation constraints
3. **Hierarchical Relationships**: Support for hierarchical data structures within domains
4. **Cross-domain Relationships**: Clear modeling of relationships between master data domains
5. **Extensibility**: Support for custom attributes and extensions

## Standard Entity Structure

All MDM entities should follow this structural pattern:

```java
@Entity
@Table(name = "entity_name")
@Getter
@Setter
public class EntityName extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Business key fields (natural keys)
    @Column(unique = true, nullable = false)
    private String businessKey;

    // Core attributes section
    // ...

    // Relationships section
    // ...

    // Metadata section
    private String status; // ACTIVE, INACTIVE, PENDING_APPROVAL, etc.
    private String sourceSystem;
    private String version;
}
```

## Versioning Strategy

For entities requiring versioning:

1. **Timestamp-based**: Use effective start/end dates to track versions over time
2. **Explicit versioning**: Use version number field with optimistic locking
3. **Snapshot tables**: Maintain separate history tables for full version history

## Data Quality Annotations

Enforce data quality using validation annotations:

```java
@NotBlank
@Size(max = 100)
private String name;

@Pattern(regexp = "^[A-Z0-9]{10}$", message = "Product code must be 10 uppercase alphanumeric characters")
private String productCode;

@Email
private String contactEmail;
```

## Relationship Modeling

1. **Parent-Child**: Use bidirectional relationships with proper cascade types
2. **Reference Data**: Use many-to-one relationships to reference data entities
3. **Cross-domain**: Define clear relationships between master data domains

## Performance Considerations

1. Use appropriate fetch types (LAZY vs EAGER)
2. Define proper indexing strategy
3. Consider denormalization for frequently accessed data
4. Use batch operations for bulk processing
