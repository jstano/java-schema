# Schema Migration Script Generator — Plan

## Context

The project already generates full CREATE DDL from a schema XML file. The goal is to add a new capability: given two versions of the schema XML (current vs. a prior git commit), produce a SQL migration script containing only the `ALTER TABLE`, `CREATE INDEX`, `DROP CONSTRAINT`, etc. statements needed to bring an existing database from the old version to the new one.

The user is open to leveraging AI, which is relevant for handling the hardest cases (column/table renames, complex type changes).

---

## Complexity Assessment

**Why a SQL text diff won't work:** Diffing two generated SQL files produces line-level differences. Those deltas are not directly executable — a changed column produces a removed and added `CREATE TABLE` line, not the `ALTER TABLE ALTER COLUMN` the database needs. The diff must happen at the **model level**, with database-specific ALTER SQL generated from the diff results.

**Overall difficulty: Medium-High (~2–3 weeks for a solid PostgreSQL implementation).**

The existing codebase is well-structured for this extension. The model objects (`Schema`, `Table`, `Column`, `Key`, `Relation`, `EnumType`) contain everything needed for comparison. The main new work is:
1. A schema diff engine (structural comparison)
2. ALTER SQL generators (new database-specific code)
3. Git integration to retrieve the prior schema XML

---

## Recommended Approach: Model-Level Diff + Optional AI Augmentation

### Phase 1 — New `schema-migrator` module (pure Java, ~2 weeks)

**Module structure:**
```
schema-migrator/
  src/main/java/com/stano/schema/migrator/
    diff/
      SchemaDiff.java            -- compares two Schema objects, returns list of SchemaChange
      SchemaChange.java          -- sealed interface / base class
      changes/                   -- one class per change type
        AddTableChange.java
        DropTableChange.java
        AddColumnChange.java
        DropColumnChange.java
        ModifyColumnChange.java  -- type, nullable, default, length changed
        AddKeyChange.java
        DropKeyChange.java
        AddRelationChange.java
        DropRelationChange.java
        AddEnumValueChange.java
        ModifyViewChange.java
        ModifyFunctionChange.java
    generator/
      MigrationSQLGenerator.java         -- abstract base
      MigrationSQLGeneratorFactory.java
      postgresql/
        PostgreSQLMigrationGenerator.java     -- handles all change types for PostgreSQL
    git/
      GitSchemaLoader.java               -- runs `git show <ref>:path` via ProcessBuilder
    GenMigration.java                    -- CLI entry point (mirrors GenSQL)
```

**`SchemaDiff` logic — what gets compared:**

| Element | Change types detected |
|---------|-----------------------|
| Tables | Added, dropped |
| Columns per table | Added, dropped, type changed, length/scale changed, nullable changed, default changed |
| Keys per table | Added (primary/unique/index), dropped |
| Relations per table | Added, dropped |
| Enum types | Values added (PG supports `ALTER TYPE ADD VALUE`; value removal is unsupported in PG) |
| Views | Body changed → DROP + CREATE |
| Functions/Procedures | Body changed → DROP + CREATE |

**What is deliberately out of scope for Phase 1:**
- Column/table renames (algorithmically ambiguous — a drop+add looks the same)
- Dropped tables (too destructive to auto-generate without explicit opt-in flag)
- MySQL, SQL Server, H2 migration generators (architecture supports adding them later)

**Git integration (`GitSchemaLoader`):**
```java
// Simple: shell out to git, no JGit dependency needed
Process p = new ProcessBuilder("git", "show", ref + ":" + repoRelativePath).start();
// ref can be: HEAD~1, a commit SHA, a tag, a branch name
```

**CLI usage:**
```
GenMigration <target-database> <schema-file> [--from-ref=HEAD~1] [--from-file=prior.xml]
  # --from-ref   : git ref to compare against (default: HEAD~1)
  # --from-file  : explicit prior XML file (skips git)
```

**`PGSQLMigrationGenerator` — representative SQL output:**
```sql
-- Added column
ALTER TABLE MyTable ADD COLUMN NewCol character varying(100);
ALTER TABLE MyTable ALTER COLUMN NewCol SET NOT NULL;

-- Dropped column
ALTER TABLE MyTable DROP COLUMN OldCol;

-- Type change
ALTER TABLE MyTable ALTER COLUMN Amount TYPE numeric(19,4) USING Amount::numeric(19,4);

-- New unique key
ALTER TABLE MyTable ADD CONSTRAINT uq_mytable_name UNIQUE (Name);

-- New index
CREATE INDEX idx_mytable_status ON MyTable(Status);

-- Drop index
DROP INDEX idx_mytable_old;

-- New foreign key
ALTER TABLE ChildTable ADD CONSTRAINT fk_childtable_parentid
  FOREIGN KEY (ParentID) REFERENCES ParentTable(ID) ON DELETE CASCADE;

-- Enum value added
ALTER TYPE MyStatusEnum ADD VALUE 'PENDING';

-- View recreated
DROP VIEW IF EXISTS MyView;
CREATE VIEW MyView AS ...;
```

### Phase 2 — AI Augmentation (optional, ~2–3 days)

Add a `--use-ai` flag to `GenMigration`. When specified:
1. Run the model-level diff as normal for structural changes
2. For changes that are potentially renames or ambiguous type coercions, serialize both XML schemas and the structural diff into a prompt
3. Call the Claude API (Anthropic SDK) to produce refined ALTER SQL for those specific changes
4. Merge AI output into the final migration script

This is most useful for detecting renames (a dropped + added column with similar names) and generating the correct `USING` clause for complex type casts.

---

## Files to Create

| File | Purpose |
|------|---------|
| `schema-migrator/build.gradle.kts` | New module config; depends on `schema-model`, `schema-parser`, `schema-sql-generator` |
| `settings.gradle.kts` | Add `include("schema-migrator")` |
| `SchemaDiff.java` | Core comparison engine |
| `SchemaChange` subtypes | One per change category |
| `PGSQLMigrationGenerator.java` | PostgreSQL ALTER SQL output |
| `GitSchemaLoader.java` | `git show` integration |
| `GenMigration.java` | CLI entry point |

## Files to Modify

| File | Change |
|------|--------|
| `settings.gradle.kts` | Add `include("schema-migrator")` |
| `schema-bom/build.gradle.kts` | Add `schema-migrator` to BOM |

---

## Verification

1. **Unit tests**: `SchemaDiff` with two hand-crafted `Schema` objects — assert correct `SchemaChange` list
2. **Integration test**: Compare `test-schema.xml` with a slightly modified copy — verify the SQL output contains exactly the expected ALTER statements
3. **Git test**: Commit a schema, modify it, run `GenMigration --from-ref=HEAD~1` — verify output is a valid runnable script
4. **Manual**: Apply the migration script to a live PostgreSQL database that has the old schema installed; verify the resulting structure matches what `GenSQL` would generate from scratch for the new version

---

## Effort Estimate

| Phase | Effort |
|-------|--------|
| Module scaffolding + Git loader | 0.5 days |
| `SchemaDiff` engine | 2 days |
| `PGSQLMigrationGenerator` (all change types) | 3 days |
| CLI + tests | 2 days |
| AI augmentation (Phase 2, optional) | 2–3 days |
| **Total Phase 1** | **~1.5 weeks** |
