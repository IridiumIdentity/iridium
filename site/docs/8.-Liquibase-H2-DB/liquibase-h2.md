# Introduction of Liquibase

### Liquibase can help your team deliver database changes faster, easier, and safer.

## What problem does liquibase solve?

In those enterprise teams who do not use liquibase, developers try to contribute SQL DML statements along with their JAVA code
changes before each release.<br>

The DBAs would combine each SQL statements into one SQL script files and run them on each database of different envs 
(dev, qa, preprod, prod) before the JAVA code get deployed.<br>

However, although individual SQL statement works locally, but when multiple SQL statements were combined into one
SQL script file, it may contain errors causing the failure of execution the following SQL statements, and it is difficult to 
locate the error SQL script file. The deployment of the JAVA code for this release will also fail due to the SQL script execution failure.
<br>


## How can liquibase help to solve the above problem?
Liquiase scripts located inside the **resource** folder of your sprintboot application. When you run your springboot application,
the liquibase script will be executed to make changes in databases before your springboot JAVA code starts.

1. In relational database like Mysql, MariaDB, Orale and PostgresQL, liquibase script only run once whether
   it is completed successfully or failed, because the liquibase framework will initially create a table named **databasechangeloglock**
   in databases  to record the changeset number. When new liquibse script codes were executed, their change set number will be
   saved to this table. When you run your springboot application next time, the liquibase script in the previous change set will 
   be ignored because its change set number exists in the **databasechangeloglock** table.

2. In in-memory database H2, when your springboot application starts, the database will be created freshly, and all liquibase 
   code will be executed from the beginning to instruct the creation of new tables, setting up new pf-fk relations, creating 
   indexes. So the combination of liquibase and h2 is a good testing tool to validate your liquibase script in your local 
   computer. If there is any errors exit in the liquibase script, your springboot application will not be stated successfully, 
   and the error log in your console could help you find the error easily. So the errors in your liquibase scrip will be found
   locally and will not hurt the shared databases in dev, qa, uat, prod envs.

