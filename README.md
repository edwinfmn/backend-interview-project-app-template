# NinjaOne Backend Interview Project

In order to fulfill all features requested in [Instructions](INSTRUCTIONS.md) file. There are the details of the development done.

## Entity Model

A basic data model and persistence was implemented creating the entities that include the properties detailed on Instructions file. 

**Device**

| ID | SystemName | Type |

**Services**

| ID | ServiceName | Type | Price | 

**ServiceAssign**

| ID | Device | Service |

*Every entity has repository, service and controller.*

## Endpoints Documentation

All endpoints documentation are completed in Swagger

Once started the project you can check the documentation [here](http://localhost:8080/swagger-ui.html#/)

## Rudimentary Cache

A basic model was created and used as **Cached Object** this object is updated on any new service assignation to a device.

If the cached object was correctly updated the message will show "Cached - Services assignment summary" when asking the Total Cost Endpoint.

```json
{
  "status": "OK",
  "message": "Cached - Services assignment summary",
  "data": {
    "totalCost": 65.00,
    ...
  }
}
```

## Continuous Integration

Helping the code validation, there is an automatic task performed by Github Actions that is responsable of build and execute test of complete project on every push, a report with final status (*SUCCESSFULL* or *ERROR*) is delivered via email.

## Starting the Application

Run the `BackendInterviewProjectApplication` class or execute `gradle bootRun` command on the project root folder.

Go to endpoints:
* [List all Devices](http://localhost:8080/device/all)
* [List all Services](http://localhost:8080/service/all)
* [List all Services assigned to Devices](http://localhost:8080/servicebydevice/all)

You should see results for all of these endpoints. 

The application is working and connected to the H2 database and it has demo data detailed in [data.sql](src/main/resources/data.sql) file.

As requested, the result of calculating total costs can be found in the following endpoint: 

[Calculate Total Costs](http://localhost:8080/servicebydevice/total)

## Testing Suite

Run the command `gradle test` command on the project root folder, after *SUCCESSFULL* ending you can find the test results in build - reports folder.

You can access here to [**Test Result Report**](build/reports/tests/test/index.html)

## H2 Console 

In order to see and interact with your db, access the h2 console in your browser.
After running the application, go to:

>http://localhost:8080/h2-console

Enter the information for the url, username, and password in the application.yml:

```yml
url: jdbc:h2:mem:localdb
username: sa 
password: password
```

You should be able to see a db console now that has the Sample Repository in it.

Type:

```sql
SELECT * FROM DEVICE;
````

Click `Run`, you should see five rows, it corresponds to starting demo data.

## Code Analysis

Sonarcloud Report is automatically generated on every pull-request or push, in this case using Sonarcloud free for Github public projects.

## With Unlimited time

If there is more time available these features could be done:

- Implement Spring Security with *OAuth 2.0* protocol and *JWT* authentication
- Add users management, creating a new feature or integrating with a service like [*Auth0*](https://auth0.com/intro-to-iam/what-is-oauth-2)
- Create an extra level to group by Customer or by Date all Services Assigned to Devices
- Create an endpoint to list all services associated to one device.
- Any audit schema - in order to maintain user / time info in every register of database.
- Reporting Endpoint 
