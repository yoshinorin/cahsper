# Development documentation

## Create scheme

```sql
CREATE DATABASE cahsper;
```

## Test

Run all tests

```
$ sbt test
```

Run specific test

```
$ sbt
$ testOnly *xxxxxSpec
```

Generate Coverage report

```
$ sbt coverageReport

or

$ sbt clean coverage test coverageReport
```
