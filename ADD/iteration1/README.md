# Iteration 1

**Step 1**

- Goal: Review Inputs

- Possible Questions:

|Question|Answer|
|--------|------|
|Inputs available and correct?|As far as the feedback of the stakeholder, the defined architectural drivers are correct|
|All drivers available?|As far as what was retained from the domain problem and the stakeholder feedback, all drivers have been defined|
|Is it clearly stablished what is the purpose for the design activities?|Yes, the purpose of this iteration is to structure the application architecture in a coarse-view|
|Have primary functionality and quality attribute scenarios been prioritized (ideally by the most important project stakeholders)?|Yes|
|Are initial architectural concerns defined?|Yes|

**Step 2**

- Goal: Establish iteration goal by selecting drivers

#### Kanban Board

| Not Addressed | Partially Addressed | Addressed |
|---------------|---------------------|-----------|
| UC-1 |||
| UC-2 |||
| UC-3 |||
| UC-4 |||
| UC-5 |||
| UC-6 |||
| UC-7 |||
| UC-11 |||
| UC-12 |||
| CON-1 |||
| CON-2 |||
| CON-3 |||
| CRN-1 |||
| QA-1 |||
| QA-2 |||
| QA-3 |||

**Step 3**

- Goal: Choose elements of the system to refine

As this is the first iteration of a greenfield system, it is necessary to define the first element of the system which is the system itself. The system is composed by two components, which are:

- IPEM (ipp-ementa mobile), which is the mobile frontend of ipp-ementa, producing a graphic interface that will be consumed by the users. This element represents an Android application.
- IPED (ipp-ementa distributor), which is the backend of ipp-ementa, producing an interface that allows the consume of business logic functionalities.

**Step 4**

- Goal: Choose one or more design concepts that satisfy the selected drivers

Given the iteration goal selected drivers in Step 2, it is necessary to define which design concepts will be taken in account to realize the elements to refine selected in Step 3. The design concents proposed are the following:

|Design Decisions and Location|Rationale|
|-----------------------------|---------|
|Use *Android KitKat* (Android API 19) as the minimal platform supported|In order to implement the mobile application in Android, it is first necessary to decide which mininal Android API is supported. As of [November of 2019](https://developer.android.com/about/dashboards), only 3.8% of Android devices are built in a platform that is lower than Android KitKat, which makes Android API 19 applications supported by 96.2% of all devices. The use of this platform will comply with CON-2 and support UC-1, UC-2, UC-3, UC-4, UC-7, UC-11, UC-12 and QA-3|
|Use [`mapsforge`](https://github.com/mapsforge/mapsforge) for maps rendering, and routes navigation|Mapsforge is a **free** open-source, offline *vector map library and writer* for Android (>= API Level 9), Windows, macOS and Linux which allows map rendering, route planning and navigation by consuming [Open Street Map](https://www.openstreetmap.org) database. Mapsforge. The use of this technology will support the concretization of UC5, UC6 and UC12 and complies with CON-2, CON-3 and QA-3|
|Use [*Firebase Cloud Messaging*](https://firebase.google.com/docs/cloud-messaging) (FCM) for application push-notifications|The use of FCM will allow the trigger of push notifications regarding favourite dishes availability, supporting the concretization of UC-11, QA-3 and complies with CON-2 and CON-3|
|Use *SQLite* for storing data|The consume of a local SQLite database will allow the store application data such as available canteens, schools, menus and dishes, supporting the concretization of UC-12, QA-3 and comply with CON-2 and CON-3|

|Alternative|Rationale|
|-----------|---------|
|Google Maps|Google Maps SDK could be used to render map and show route navigation, yet it requires a monthly subscription which violates CON-3. The only benefit it provides is that it is a well known and tested solution provided by Google|
|JSON files for data storing|A persistence strategy that uses JSON files could also be used for data storing (UC-12), but would require more testing effort as this strategy would require files manipulation. SQLite on the other hand is a well tested solution that already manages itself data manipulation and only uses one file|

**Step 5**

- Goal: Instantiate architectural elements, allocate responsibilities and define interfaces

To satisfy the structure of the chosen design concepts, the following elements are proposed to be created:

|Design Decisions and Location|Rationale|
|-----------------------------|---------|
|Map system elements to logical components|This will help understanding how each system element communicates with each other|
|Map system elements to physical components|This will help understanding how each system element physically communicates with each other|