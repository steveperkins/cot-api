# cot-api
## What is cot-api
Open-source textbook search API for collegeopentextbooks.org.

## Start developing
1. Fork and clone this project
1. Run the contents of `src/main/resources/init-db.sql` against your local Postgres server to create a new database, new database user, and generate the COT schema
1. Start coding! Run org.collegeopentextbooks.api.Application.java to launch a local API server at http://localhost:8080.

If you'd like some test data, start with a fresh database and run org.collegeopentextbooks.api.TestDataGenerator.java.

## Contribute your work
When you're finished with a feature, create a new pull request to the test branch in Github. The project maintainers will review the changes and approve.
